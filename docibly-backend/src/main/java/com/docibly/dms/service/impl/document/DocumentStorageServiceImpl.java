package com.docibly.dms.service.impl.document;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.bean.core.enums.AuditAction;
import com.docibly.dms.bean.core.enums.DigitizeFormat;
import com.docibly.dms.bean.core.enums.DocumentStatus;
import com.docibly.dms.bean.core.enums.DocumentVisibility;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.enums.OcrStatus;
import com.docibly.dms.bean.core.folder.Folder;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.bean.core.tag.Tag;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.config.storage.StorageProperties;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.dao.facade.core.document.DocumentDao;
import com.docibly.dms.dao.facade.core.document.DocumentVersionDao;
import com.docibly.dms.dao.facade.core.folder.FolderDao;
import com.docibly.dms.dao.facade.core.organization.OrganizationDao;
import com.docibly.dms.dao.facade.core.tag.TagDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import com.docibly.dms.service.facade.document.DocumentStorageService;
import com.docibly.dms.service.facade.ocr.OcrProcessingService;
import com.docibly.dms.service.facade.auditlog.AuditLogger;
import com.docibly.dms.service.facade.searchindex.DocumentIndexerService;
import com.docibly.dms.service.facade.storage.StorageService;
import com.docibly.dms.ws.dto.storage.FileMetadata;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DocumentStorageServiceImpl implements DocumentStorageService {

    private static final Logger log = LoggerFactory.getLogger(DocumentStorageServiceImpl.class);
    private static final String STORAGE_FOLDER = "documents";

    private final StorageService storageService;
    private final StorageProperties storageProperties;
    private final DocumentDao documentDao;
    private final DocumentVersionDao documentVersionDao;
    private final OrganizationDao organizationDao;
    private final FolderDao folderDao;
    private final TagDao tagDao;
    private final UserDao userDao;
    private final OrgAuthorizationService orgAuth;
    private final OcrProcessingService ocrProcessingService;
    private final DocumentIndexerService documentIndexerService;
    private final AuditLogger auditLogger;

    @Override
    @Transactional
    public Document upload(Long orgId, MultipartFile file, String title,
                           String description, Long folderId, List<Long> tagIds) {
        return upload(orgId, file, title, description, folderId, tagIds, DigitizeFormat.NONE);
    }

    @Override
    @Transactional
    public Document upload(Long orgId, MultipartFile file, String title,
                           String description, Long folderId, List<Long> tagIds,
                           DigitizeFormat digitizeFormat) {
        orgAuth.requireRole(orgId, MemberRole.MEMBER);

        Organization organization = organizationDao.findById(orgId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORG_NOT_FOUND));

        User currentUser = getCurrentUser();

        FileMetadata metadata = storageService.upload(file, STORAGE_FOLDER);

        String uniqueFilename = metadata.objectKey().contains("/")
                ? metadata.objectKey().substring(metadata.objectKey().lastIndexOf('/') + 1)
                : metadata.objectKey();

        Document document = Document.builder()
                .title(title)
                .description(description)
                .originalFilename(file.getOriginalFilename())
                .storedFilename(uniqueFilename)
                .mimeType(metadata.contentType())
                .fileSizeBytes(metadata.size())
                .storageBucket(storageProperties.getBucket())
                .storageKey(metadata.objectKey())
                .status(DocumentStatus.PUBLISHED)
                .visibility(DocumentVisibility.ORGANIZATION)
                .currentVersionNumber(1)
                .downloadCount(0)
                .viewCount(0)
                .isPasswordProtected(false)
                .ocrStatus(OcrStatus.PENDING)
                .digitizeFormat(digitizeFormat)
                .organization(organization)
                .build();

        if (folderId != null) {
            Folder folder = folderDao.findById(folderId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORG_NOT_FOUND));
            document.setFolder(folder);
        }

        if (tagIds != null && !tagIds.isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagDao.findAllById(tagIds));
            document.setTags(tags);
        }

        Document saved = documentDao.save(document);

        DocumentVersion version = DocumentVersion.builder()
                .versionNumber(1)
                .label("Initial upload")
                .originalFilename(file.getOriginalFilename())
                .storedFilename(uniqueFilename)
                .storageKey(metadata.objectKey())
                .fileSizeBytes(metadata.size())
                .mimeType(metadata.contentType())
                .isCurrentVersion(true)
                .ocrStatus(OcrStatus.PENDING)
                .document(saved)
                .uploadedBy(currentUser)
                .build();

        documentVersionDao.save(version);

        documentIndexerService.indexDocument(saved);

        log.info("Document uploaded: id={}, title={}, orgId={}, storageKey={}",
                saved.getId(), title, orgId, metadata.objectKey());

        auditLogger.log(AuditAction.DOCUMENT_UPLOADED, "Document", saved.getId().toString(),
                orgId, saved.getTitle());

        ocrProcessingService.processDocument(saved.getId(), digitizeFormat);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Resource download(Long documentId) {
        Document document = documentDao.findById(documentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DOCUMENT_NOT_FOUND));

        orgAuth.requireRole(document.getOrganization().getId(), MemberRole.VIEWER);

        document.setDownloadCount(document.getDownloadCount() != null
                ? document.getDownloadCount() + 1 : 1);
        documentDao.save(document);

        try {
            InputStream stream = storageService.download(document.getStorageKey());
            return new InputStreamResource(stream);
        } catch (Exception e) {
            log.error("Failed to download document {}: {}", documentId, e.getMessage());
            throw new BusinessException(ErrorCode.DOCUMENT_DOWNLOAD_FAILED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getPresignedUrl(Long documentId) {
        Document document = documentDao.findById(documentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DOCUMENT_NOT_FOUND));

        orgAuth.requireRole(document.getOrganization().getId(), MemberRole.VIEWER);

        document.setViewCount(document.getViewCount() != null
                ? document.getViewCount() + 1 : 1);
        documentDao.save(document);

        return storageService.presignGet(document.getStorageKey(),
                Duration.ofSeconds(storageProperties.getPresignedUrlExpirySeconds()));
    }

    private User getCurrentUser() {
        var auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new BusinessException(ErrorCode.ORG_MEMBER_NOT_FOUND);
        }
        return userDao.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
