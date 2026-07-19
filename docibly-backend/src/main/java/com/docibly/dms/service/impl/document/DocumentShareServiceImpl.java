package com.docibly.dms.service.impl.document;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.document.DocumentShare;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.enums.AuditAction;
import com.docibly.dms.bean.core.enums.SharePermission;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.document.DocumentDao;
import com.docibly.dms.dao.facade.core.document.DocumentShareDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import com.docibly.dms.service.facade.document.DocumentService;
import com.docibly.dms.service.facade.document.DocumentShareService;
import com.docibly.dms.service.facade.document.DocumentStorageService;
import com.docibly.dms.service.facade.auditlog.AuditLogger;
import com.docibly.dms.service.facade.notification.NotificationFiringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentShareServiceImpl implements DocumentShareService {

    private final DocumentShareDao documentShareDao;
    private final DocumentDao documentDao;
    private final UserDao userDao;
    private final OrgAuthorizationService orgAuth;
    private final DocumentStorageService documentStorageService;
    private final PasswordEncoder passwordEncoder;
    private final NotificationFiringService notificationFiringService;
    private final AuditLogger auditLogger;

    @Override
    @Transactional
    public DocumentShare createShare(Long documentId, Long orgId, SharePermission permission,
                                     LocalDateTime expiresAt, String password) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);

        Document document = documentDao.findById(documentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DOCUMENT_NOT_FOUND));

        User currentUser = getCurrentUser();

        DocumentShare share = DocumentShare.builder()
                .shareToken(UUID.randomUUID().toString())
                .permission(permission != null ? permission : SharePermission.VIEW)
                .isPublicLink(true)
                .expiresAt(expiresAt)
                .isRevoked(false)
                .accessCount(0)
                .document(document)
                .sharedBy(currentUser)
                .build();

        if (password != null && !password.isBlank()) {
            share.setRequiresPassword(true);
            share.setPasswordHash(passwordEncoder.encode(password));
        } else {
            share.setRequiresPassword(false);
        }

        share = documentShareDao.save(share);
        log.info("Share created: token={}, docId={}, permission={}", share.getShareToken(), documentId, permission);
        auditLogger.log(AuditAction.SHARE_CREATED, "DocumentShare", share.getId().toString(),
                orgId, "documentId=" + documentId + ", permission=" + permission);
        return share;
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentShare findByToken(String token) {
        return documentShareDao.findByShareToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHARE_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentShare verifyPassword(String token, String password) {
        DocumentShare share = findByToken(token);

        if (share.getRequiresPassword() != null && share.getRequiresPassword()) {
            if (password == null || password.isBlank()) {
                throw new BusinessException(ErrorCode.SHARE_PASSWORD_REQUIRED);
            }
            if (!passwordEncoder.matches(password, share.getPasswordHash())) {
                throw new BusinessException(ErrorCode.SHARE_PASSWORD_INVALID);
            }
        }

        return share;
    }

    @Override
    @Transactional
    public DocumentInfo accessShare(String token, String password) {
        DocumentShare share = verifyPassword(token, password);
        validateShareAccessible(share);

        share.setAccessCount(share.getAccessCount() != null ? share.getAccessCount() + 1 : 1);
        share.setLastAccessedAt(LocalDateTime.now());
        documentShareDao.save(share);

        Document document = share.getDocument();
        Resource resource = documentStorageService.download(document.getId());

        log.info("Share accessed: token={}, docId={}", token, document.getId());
        return new DocumentInfo(share, document, resource);
    }

    @Override
    @Transactional
    public void revokeShare(Long shareId, Long orgId) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);

        DocumentShare share = documentShareDao.findById(shareId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHARE_NOT_FOUND));

        share.setIsRevoked(true);
        share.setRevokedAt(LocalDateTime.now());
        documentShareDao.save(share);

        auditLogger.log(AuditAction.SHARE_REVOKED, "DocumentShare", shareId.toString(),
                orgId, "token=" + share.getShareToken());

        log.info("Share revoked: id={}, token={}", shareId, share.getShareToken());
    }

    private void validateShareAccessible(DocumentShare share) {
        if (Boolean.TRUE.equals(share.getIsRevoked())) {
            throw new BusinessException(ErrorCode.SHARE_REVOKED);
        }
        if (share.getExpiresAt() != null && share.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.SHARE_EXPIRED);
        }
    }

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            return userDao.findById(user.getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        }
        return null;
    }

    // ── Delegated CRUD methods (generated by Archeon) ──

    @Override
    @Transactional
    public DocumentShare create(DocumentShare t) { return save(t); }

    @Override
    @Transactional
    public DocumentShare save(DocumentShare entity) {
        return documentShareDao.save(entity);
    }

    @Override
    @Transactional
    public DocumentShare update(DocumentShare t) {
        if (t == null || t.getId() == null) return null;
        return documentShareDao.findById(t.getId()).map(existing -> {
            if (t.getShareToken() != null) existing.setShareToken(t.getShareToken());
            if (t.getPermission() != null) existing.setPermission(t.getPermission());
            if (t.getSharedWithEmail() != null) existing.setSharedWithEmail(t.getSharedWithEmail());
            if (t.getIsPublicLink() != null) existing.setIsPublicLink(t.getIsPublicLink());
            if (t.getExpiresAt() != null) existing.setExpiresAt(t.getExpiresAt());
            if (t.getIsRevoked() != null) existing.setIsRevoked(t.getIsRevoked());
            if (t.getRevokedAt() != null) existing.setRevokedAt(t.getRevokedAt());
            if (t.getAccessCount() != null) existing.setAccessCount(t.getAccessCount());
            if (t.getLastAccessedAt() != null) existing.setLastAccessedAt(t.getLastAccessedAt());
            if (t.getRequiresPassword() != null) existing.setRequiresPassword(t.getRequiresPassword());
            if (t.getPasswordHash() != null) existing.setPasswordHash(t.getPasswordHash());
            return documentShareDao.save(existing);
        }).orElse(null);
    }

    @Override
    public List<DocumentShare> update(List<DocumentShare> ts, boolean createIfNotExist) { return List.of(); }

    @Override
    public Optional<DocumentShare> findById(Long id) { return documentShareDao.findById(id); }

    @Override
    public void deleteById(Long id) {
        documentShareDao.findById(id).ifPresent(share -> {
            share.setDeletedAt(LocalDateTime.now());
            documentShareDao.save(share);
        });
    }

    @Override
    public Optional<DocumentShare> findAndDeleteById(Long id) {
        return documentShareDao.findById(id).map(share -> {
            share.setDeletedAt(LocalDateTime.now());
            documentShareDao.save(share);
            return share;
        });
    }

    @Override
    public DocumentShare findOrSave(DocumentShare t) { return null; }

    @Override
    public DocumentShare findByReferenceEntity(DocumentShare t) { return null; }

    @Override
    public DocumentShare findWithAssociatedLists(Long id) {
        return documentShareDao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    public List<DocumentShare> findAll() { return documentShareDao.findAll(); }

    @Override
    public List<DocumentShare> findByCriteria(com.docibly.dms.dao.criteria.core.document.DocumentShareCriteria criteria) {
        return documentShareDao.findAll();
    }

    @Override
    public Page<DocumentShare> findPaginatedByCriteria(com.docibly.dms.dao.criteria.core.document.DocumentShareCriteria criteria, org.springframework.data.domain.Pageable pageable) {
        return documentShareDao.findAll(pageable);
    }

    @Override
    public int getDataSize(com.docibly.dms.dao.criteria.core.document.DocumentShareCriteria criteria) { return (int) documentShareDao.count(); }

    @Override
    public List<DocumentShare> delete(List<DocumentShare> ts) { return List.of(); }

    @Override
    public DocumentShare findByRef(String ref) { return null; }
}
