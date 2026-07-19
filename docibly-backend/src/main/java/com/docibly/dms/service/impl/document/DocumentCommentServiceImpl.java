package com.docibly.dms.service.impl.document;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.document.DocumentComment;
import com.docibly.dms.bean.core.enums.AuditAction;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.document.DocumentCommentDao;
import com.docibly.dms.dao.facade.core.document.DocumentDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import com.docibly.dms.service.facade.auditlog.AuditLogger;
import com.docibly.dms.service.facade.document.DocumentCommentService;
import com.docibly.dms.service.facade.notification.NotificationFiringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentCommentServiceImpl implements DocumentCommentService {

    private final DocumentCommentDao documentCommentDao;
    private final DocumentDao documentDao;
    private final UserDao userDao;
    private final OrgAuthorizationService orgAuth;
    private final NotificationFiringService notificationFiringService;
    private final AuditLogger auditLogger;

    @Override
    @Transactional
    public DocumentComment addComment(Long documentId, Long orgId, String content, Long parentCommentId) {
        orgAuth.requireRole(orgId, MemberRole.MEMBER);

        Document document = documentDao.findById(documentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DOCUMENT_NOT_FOUND));

        User currentUser = getCurrentUser();

        DocumentComment parentComment = null;
        if (parentCommentId != null) {
            parentComment = documentCommentDao.findById(parentCommentId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
            if (!parentComment.getDocument().getId().equals(documentId)) {
                throw new BusinessException(ErrorCode.COMMENT_PARENT_MISMATCH);
            }
        }

        DocumentComment comment = DocumentComment.builder()
                .content(content)
                .isResolved(false)
                .isEdited(false)
                .document(document)
                .parentComment(parentComment)
                .author(currentUser)
                .build();
        comment = documentCommentDao.save(comment);

        User docOwner = document.getCreatedBy() != null
                ? userDao.findById(document.getCreatedBy()).orElse(null) : null;
        if (docOwner != null && !docOwner.getId().equals(currentUser.getId())) {
            notificationFiringService.commentAdded(docOwner.getId(),
                    document.getTitle(), currentUser.getEmail(), documentId);
        }

        log.info("Comment added: docId={}, commentId={}, parentId={}", documentId, comment.getId(), parentCommentId);

        auditLogger.log(AuditAction.DOCUMENT_UPDATED, "DocumentComment", comment.getId().toString(),
                orgId, "documentId=" + documentId + ", action=comment_added");

        return comment;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentComment> getComments(Long documentId, Long orgId) {
        orgAuth.requireRole(orgId, MemberRole.VIEWER);
        return documentCommentDao.findByDocument_IdAndParentCommentIsNullOrderByCreatedDateAsc(documentId);
    }

    @Override
    @Transactional
    public DocumentComment resolveComment(Long commentId, Long orgId) {
        DocumentComment comment = documentCommentDao.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        orgAuth.requireRole(comment.getDocument().getOrganization().getId(), MemberRole.MEMBER);

        comment.setIsResolved(!Boolean.TRUE.equals(comment.getIsResolved()));
        comment.setResolvedAt(Boolean.TRUE.equals(comment.getIsResolved()) ? LocalDateTime.now() : null);
        comment = documentCommentDao.save(comment);

        log.info("Comment {} resolved={}", commentId, comment.getIsResolved());
        return comment;
    }

    @Override
    @Transactional
    public DocumentComment editComment(Long commentId, Long orgId, String content) {
        DocumentComment comment = documentCommentDao.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        orgAuth.requireRole(comment.getDocument().getOrganization().getId(), MemberRole.MEMBER);

        User currentUser = getCurrentUser();
        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.COMMENT_NOT_OWNER);
        }

        comment.setContent(content);
        comment.setIsEdited(true);
        comment.setEditedAt(LocalDateTime.now());
        comment = documentCommentDao.save(comment);

        log.info("Comment edited: commentId={}", commentId);
        return comment;
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

    // ── Generated CRUD delegates ──

    @Override
    public DocumentComment create(DocumentComment t) { return save(t); }

    @Override
    @Transactional
    public DocumentComment save(DocumentComment entity) {
        return entity != null ? documentCommentDao.save(entity) : null;
    }

    @Override
    @Transactional
    public DocumentComment update(DocumentComment t) {
        if (t == null || t.getId() == null) return null;
        return documentCommentDao.findById(t.getId()).map(existing -> {
            if (t.getContent() != null) existing.setContent(t.getContent());
            if (t.getIsResolved() != null) existing.setIsResolved(t.getIsResolved());
            if (t.getResolvedAt() != null) existing.setResolvedAt(t.getResolvedAt());
            if (t.getPageNumber() != null) existing.setPageNumber(t.getPageNumber());
            if (t.getPositionX() != null) existing.setPositionX(t.getPositionX());
            if (t.getPositionY() != null) existing.setPositionY(t.getPositionY());
            if (t.getIsEdited() != null) existing.setIsEdited(t.getIsEdited());
            if (t.getEditedAt() != null) existing.setEditedAt(t.getEditedAt());
            return documentCommentDao.save(existing);
        }).orElse(null);
    }

    @Override public List<DocumentComment> update(List<DocumentComment> ts, boolean createIfNotExist) { return List.of(); }
    @Override public Optional<DocumentComment> findById(Long id) { return documentCommentDao.findById(id); }

    @Override @Transactional
    public void deleteById(Long id) {
        documentCommentDao.findById(id).ifPresent(c -> {
            c.setDeletedAt(LocalDateTime.now());
            documentCommentDao.save(c);
        });
    }

    @Override public Optional<DocumentComment> findAndDeleteById(Long id) {
        return documentCommentDao.findById(id).map(c -> {
            c.setDeletedAt(LocalDateTime.now());
            documentCommentDao.save(c);
            return c;
        });
    }

    @Override public DocumentComment findOrSave(DocumentComment t) { return null; }
    @Override public DocumentComment findByReferenceEntity(DocumentComment t) { return null; }
    @Override public DocumentComment findWithAssociatedLists(Long id) { return documentCommentDao.findWithAssociationsById(id).orElse(null); }
    @Override public List<DocumentComment> findAll() { return documentCommentDao.findAll(); }
    @Override public List<DocumentComment> findByCriteria(com.docibly.dms.dao.criteria.core.document.DocumentCommentCriteria criteria) { return documentCommentDao.findAll(); }
    @Override public org.springframework.data.domain.Page<DocumentComment> findPaginatedByCriteria(com.docibly.dms.dao.criteria.core.document.DocumentCommentCriteria criteria, org.springframework.data.domain.Pageable pageable) { return documentCommentDao.findAll(pageable); }
    @Override public int getDataSize(com.docibly.dms.dao.criteria.core.document.DocumentCommentCriteria criteria) { return (int) documentCommentDao.count(); }
    @Override public List<DocumentComment> delete(List<DocumentComment> ts) { return List.of(); }
    @Override public DocumentComment findByRef(String ref) { return null; }
}
