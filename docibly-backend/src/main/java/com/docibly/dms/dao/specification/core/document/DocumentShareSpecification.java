package com.docibly.dms.dao.specification.core.document;

import com.docibly.dms.dao.criteria.core.document.DocumentShareCriteria;
import com.docibly.dms.bean.core.document.DocumentShare;
import com.docibly.dms.bean.core.enums.SharePermission;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DocumentShareSpecification implements Specification<DocumentShare> {

    private final DocumentShareCriteria criteria;
    private final boolean distinct;

    public DocumentShareSpecification(DocumentShareCriteria criteria) {
        this(criteria, false);
    }

    public DocumentShareSpecification(DocumentShareCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<DocumentShare> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (distinct) {
            query.distinct(true);
        }

        // ── Base entity fields ──────────────────────────────────────
        if (criteria.getId() != null) {
            predicates.add(cb.equal(root.get("id"), criteria.getId()));
        }

        if (criteria.getRef() != null && !criteria.getRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("ref")),
                "%" + criteria.getRef().toLowerCase() + "%"));
        }

        if (criteria.getCreatedAt() != null) {
            predicates.add(cb.equal(root.get("createdDate"), criteria.getCreatedAt()));
        }
        if (criteria.getCreatedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), criteria.getCreatedAtFrom()));
        }
        if (criteria.getCreatedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), criteria.getCreatedAtTo()));
        }

        if (criteria.getUpdatedAt() != null) {
            predicates.add(cb.equal(root.get("lastModifiedDate"), criteria.getUpdatedAt()));
        }
        if (criteria.getUpdatedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastModifiedDate"), criteria.getUpdatedAtFrom()));
        }
        if (criteria.getUpdatedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("lastModifiedDate"), criteria.getUpdatedAtTo()));
        }

        // ── Entity-specific fields ──────────────────────────────────
        // shareToken - String field (supports like search)
        if (criteria.getShareToken() != null && !criteria.getShareToken().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("shareToken")),
                "%" + criteria.getShareToken().toLowerCase() + "%"));
        }
        if (criteria.getShareTokenLike() != null && !criteria.getShareTokenLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("shareToken")),
                "%" + criteria.getShareTokenLike().toLowerCase() + "%"));
        }
        // permission - Enum field
        if (criteria.getPermission() != null) {
            predicates.add(cb.equal(root.get("permission"), criteria.getPermission()));
        }
        // sharedWithEmail - String field (supports like search)
        if (criteria.getSharedWithEmail() != null && !criteria.getSharedWithEmail().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("sharedWithEmail")),
                "%" + criteria.getSharedWithEmail().toLowerCase() + "%"));
        }
        if (criteria.getSharedWithEmailLike() != null && !criteria.getSharedWithEmailLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("sharedWithEmail")),
                "%" + criteria.getSharedWithEmailLike().toLowerCase() + "%"));
        }
        // isPublicLink - Boolean field
        if (criteria.getIsPublicLink() != null) {
            predicates.add(cb.equal(root.get("isPublicLink"), criteria.getIsPublicLink()));
        }
        // expiresAt - LocalDateTime field (supports range)
        if (criteria.getExpiresAt() != null) {
            predicates.add(cb.equal(root.get("expiresAt"), criteria.getExpiresAt()));
        }
        if (criteria.getExpiresAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("expiresAt"), criteria.getExpiresAtFrom()));
        }
        if (criteria.getExpiresAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("expiresAt"), criteria.getExpiresAtTo()));
        }
        // isRevoked - Boolean field
        if (criteria.getIsRevoked() != null) {
            predicates.add(cb.equal(root.get("isRevoked"), criteria.getIsRevoked()));
        }
        // revokedAt - LocalDateTime field (supports range)
        if (criteria.getRevokedAt() != null) {
            predicates.add(cb.equal(root.get("revokedAt"), criteria.getRevokedAt()));
        }
        if (criteria.getRevokedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("revokedAt"), criteria.getRevokedAtFrom()));
        }
        if (criteria.getRevokedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("revokedAt"), criteria.getRevokedAtTo()));
        }
        // accessCount - Integer field (supports range)
        if (criteria.getAccessCount() != null) {
            predicates.add(cb.equal(root.get("accessCount"), criteria.getAccessCount()));
        }
        if (criteria.getAccessCountMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("accessCount"), criteria.getAccessCountMin()));
        }
        if (criteria.getAccessCountMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("accessCount"), criteria.getAccessCountMax()));
        }
        // lastAccessedAt - LocalDateTime field (supports range)
        if (criteria.getLastAccessedAt() != null) {
            predicates.add(cb.equal(root.get("lastAccessedAt"), criteria.getLastAccessedAt()));
        }
        if (criteria.getLastAccessedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("lastAccessedAt"), criteria.getLastAccessedAtFrom()));
        }
        if (criteria.getLastAccessedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("lastAccessedAt"), criteria.getLastAccessedAtTo()));
        }
        // requiresPassword - Boolean field
        if (criteria.getRequiresPassword() != null) {
            predicates.add(cb.equal(root.get("requiresPassword"), criteria.getRequiresPassword()));
        }
        // passwordHash - String field (supports like search)
        if (criteria.getPasswordHash() != null && !criteria.getPasswordHash().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("passwordHash")),
                "%" + criteria.getPasswordHash().toLowerCase() + "%"));
        }
        if (criteria.getPasswordHashLike() != null && !criteria.getPasswordHashLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("passwordHash")),
                "%" + criteria.getPasswordHashLike().toLowerCase() + "%"));
        }

        // ── Relationship fields (foreign key lookups) ───────────────
        // document - ManyToOne relationship
        if (criteria.getDocumentId() != null) {
            predicates.add(cb.equal(root.get("document").get("id"), criteria.getDocumentId()));
        }
        if (criteria.getDocumentRef() != null && !criteria.getDocumentRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("document").get("ref")),
                "%" + criteria.getDocumentRef().toLowerCase() + "%"));
        }
        // sharedBy - ManyToOne relationship
        if (criteria.getSharedById() != null) {
            predicates.add(cb.equal(root.get("sharedBy").get("id"), criteria.getSharedById()));
        }
        // sharedWith - ManyToOne relationship
        if (criteria.getSharedWithId() != null) {
            predicates.add(cb.equal(root.get("sharedWith").get("id"), criteria.getSharedWithId()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
