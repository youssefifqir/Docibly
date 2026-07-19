package com.docibly.dms.service.impl.document;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.bean.core.enums.AuditAction;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.enums.OcrStatus;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.document.DocumentDao;
import com.docibly.dms.dao.facade.core.document.DocumentVersionDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import com.docibly.dms.service.facade.document.DocumentService;
import com.docibly.dms.service.facade.document.DocumentVersionService;
import com.docibly.dms.service.facade.auditlog.AuditLogger;
import com.docibly.dms.service.facade.ocr.OcrProcessingService;
import com.docibly.dms.service.facade.searchindex.DocumentIndexerService;
import com.docibly.dms.service.facade.storage.StorageService;
import com.docibly.dms.ws.dto.storage.FileMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentVersionServiceImpl implements DocumentVersionService {

    private final DocumentVersionDao documentVersionDao;
    private final DocumentDao documentDao;
    private final UserDao userDao;
    private final StorageService storageService;
    private final OrgAuthorizationService orgAuth;
    private final DocumentIndexerService documentIndexerService;
    private final OcrProcessingService ocrProcessingService;
    private final AuditLogger auditLogger;
    private static final String STORAGE_FOLDER = "documents";

    @Override
    @Transactional
    public DocumentVersion reupload(Long documentId, Long orgId, MultipartFile file,
                                     String label, String changeNote) {
        orgAuth.requireRole(orgId, MemberRole.MEMBER);

        Document document = documentDao.findById(documentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DOCUMENT_NOT_FOUND));

        User currentUser = getCurrentUser();

        List<DocumentVersion> existingVersions = documentVersionDao
                .findByDocument_IdOrderByVersionNumberDesc(documentId);
        int nextVersionNumber = existingVersions.isEmpty() ? 1 : existingVersions.get(0).getVersionNumber() + 1;

        existingVersions.forEach(v -> v.setIsCurrentVersion(false));
        documentVersionDao.saveAll(existingVersions);

        FileMetadata metadata = storageService.upload(file, STORAGE_FOLDER);

        DocumentVersion newVersion = DocumentVersion.builder()
                .versionNumber(nextVersionNumber)
                .label(label != null ? label : "Version " + nextVersionNumber)
                .changeNote(changeNote)
                .originalFilename(file.getOriginalFilename())
                .storedFilename(metadata.originalName())
                .storageKey(metadata.objectKey())
                .fileSizeBytes(metadata.size())
                .mimeType(metadata.contentType())
                .isCurrentVersion(true)
                .ocrStatus(OcrStatus.PENDING)
                .document(document)
                .uploadedBy(currentUser)
                .build();
        newVersion = documentVersionDao.save(newVersion);

        document.setStoredFilename(metadata.originalName());
        document.setStorageKey(metadata.objectKey());
        document.setMimeType(metadata.contentType());
        document.setFileSizeBytes(metadata.size());
        document.setOriginalFilename(file.getOriginalFilename());
        document.setCurrentVersionNumber(nextVersionNumber);
        document.setOcrStatus(OcrStatus.PENDING);
        documentDao.save(document);

        documentIndexerService.indexDocument(document);

        ocrProcessingService.processDocument(documentId, document.getDigitizeFormat());

        log.info("Document re-uploaded: docId={}, version={}, storageKey={}",
                documentId, nextVersionNumber, metadata.objectKey());

        auditLogger.log(AuditAction.VERSION_CREATED, "DocumentVersion", newVersion.getId().toString(),
                orgId, "documentId=" + documentId + ", version=" + nextVersionNumber);

        return newVersion;
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

    // ── Delegated CRUD methods ──

    @Override
    public DocumentVersion create(DocumentVersion t) { return save(t); }

    @Override
    @Transactional
    public DocumentVersion save(DocumentVersion entity) {
        if (entity == null) return null;
        return documentVersionDao.save(entity);
    }

    @Override
    @Transactional
    public DocumentVersion update(DocumentVersion t) {
        if (t == null || t.getId() == null) return null;
        return documentVersionDao.findById(t.getId()).map(existing -> {
            if (t.getVersionNumber() != null) existing.setVersionNumber(t.getVersionNumber());
            if (t.getLabel() != null) existing.setLabel(t.getLabel());
            if (t.getChangeNote() != null) existing.setChangeNote(t.getChangeNote());
            if (t.getOriginalFilename() != null) existing.setOriginalFilename(t.getOriginalFilename());
            if (t.getStoredFilename() != null) existing.setStoredFilename(t.getStoredFilename());
            if (t.getStorageKey() != null) existing.setStorageKey(t.getStorageKey());
            if (t.getFileSizeBytes() != null) existing.setFileSizeBytes(t.getFileSizeBytes());
            if (t.getMimeType() != null) existing.setMimeType(t.getMimeType());
            if (t.getIsCurrentVersion() != null) existing.setIsCurrentVersion(t.getIsCurrentVersion());
            if (t.getOcrStatus() != null) existing.setOcrStatus(t.getOcrStatus());
            if (t.getOcrText() != null) existing.setOcrText(t.getOcrText());
            return documentVersionDao.save(existing);
        }).orElse(null);
    }

    @Override
    public List<DocumentVersion> update(List<DocumentVersion> ts, boolean createIfNotExist) { return List.of(); }
    @Override
    public Optional<DocumentVersion> findById(Long id) { return documentVersionDao.findById(id); }

    @Override
    @Transactional
    public void deleteById(Long id) {
        documentVersionDao.findById(id).ifPresent(v -> {
            v.setDeletedAt(LocalDateTime.now());
            documentVersionDao.save(v);
        });
    }

    @Override
    public Optional<DocumentVersion> findAndDeleteById(Long id) {
        return documentVersionDao.findById(id).map(v -> {
            v.setDeletedAt(LocalDateTime.now());
            documentVersionDao.save(v);
            return v;
        });
    }

    @Override
    public DocumentVersion findOrSave(DocumentVersion t) { return null; }
    @Override
    public DocumentVersion findByReferenceEntity(DocumentVersion t) { return null; }
    @Override
    public DocumentVersion findWithAssociatedLists(Long id) { return documentVersionDao.findWithAssociationsById(id).orElse(null); }
    @Override
    public List<DocumentVersion> findAll() { return documentVersionDao.findAll(); }
    @Override
    public List<DocumentVersion> findByCriteria(com.docibly.dms.dao.criteria.core.document.DocumentVersionCriteria criteria) { return documentVersionDao.findAll(); }

    @Override
    public List<DocumentVersion> findByDocumentId(Long documentId) {
        return documentVersionDao.findByDocument_IdOrderByVersionNumberDesc(documentId);
    }
    @Override
    public org.springframework.data.domain.Page<DocumentVersion> findPaginatedByCriteria(com.docibly.dms.dao.criteria.core.document.DocumentVersionCriteria criteria, org.springframework.data.domain.Pageable pageable) { return documentVersionDao.findAll(pageable); }
    @Override
    public int getDataSize(com.docibly.dms.dao.criteria.core.document.DocumentVersionCriteria criteria) { return (int) documentVersionDao.count(); }
    @Override
    public List<DocumentVersion> delete(List<DocumentVersion> ts) { return List.of(); }
    @Override
    public DocumentVersion findByRef(String ref) { return null; }
}
