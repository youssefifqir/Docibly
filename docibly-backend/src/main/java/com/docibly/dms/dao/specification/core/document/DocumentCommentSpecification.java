package com.docibly.dms.dao.specification.core.document;

import com.docibly.dms.dao.criteria.core.document.DocumentCommentCriteria;
import com.docibly.dms.bean.core.document.DocumentComment;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DocumentCommentSpecification implements Specification<DocumentComment> {

    private final DocumentCommentCriteria criteria;
    private final boolean distinct;

    public DocumentCommentSpecification(DocumentCommentCriteria criteria) {
        this(criteria, false);
    }

    public DocumentCommentSpecification(DocumentCommentCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<DocumentComment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // content - String field (supports like search)
        if (criteria.getContent() != null && !criteria.getContent().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("content")),
                "%" + criteria.getContent().toLowerCase() + "%"));
        }
        if (criteria.getContentLike() != null && !criteria.getContentLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("content")),
                "%" + criteria.getContentLike().toLowerCase() + "%"));
        }
        // isResolved - Boolean field
        if (criteria.getIsResolved() != null) {
            predicates.add(cb.equal(root.get("isResolved"), criteria.getIsResolved()));
        }
        // resolvedAt - LocalDateTime field (supports range)
        if (criteria.getResolvedAt() != null) {
            predicates.add(cb.equal(root.get("resolvedAt"), criteria.getResolvedAt()));
        }
        if (criteria.getResolvedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("resolvedAt"), criteria.getResolvedAtFrom()));
        }
        if (criteria.getResolvedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("resolvedAt"), criteria.getResolvedAtTo()));
        }
        // pageNumber - Integer field (supports range)
        if (criteria.getPageNumber() != null) {
            predicates.add(cb.equal(root.get("pageNumber"), criteria.getPageNumber()));
        }
        if (criteria.getPageNumberMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("pageNumber"), criteria.getPageNumberMin()));
        }
        if (criteria.getPageNumberMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("pageNumber"), criteria.getPageNumberMax()));
        }
        // positionX - BigDecimal field (supports range)
        if (criteria.getPositionX() != null) {
            predicates.add(cb.equal(root.get("positionX"), criteria.getPositionX()));
        }
        if (criteria.getPositionXMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("positionX"), criteria.getPositionXMin()));
        }
        if (criteria.getPositionXMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("positionX"), criteria.getPositionXMax()));
        }
        // positionY - BigDecimal field (supports range)
        if (criteria.getPositionY() != null) {
            predicates.add(cb.equal(root.get("positionY"), criteria.getPositionY()));
        }
        if (criteria.getPositionYMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("positionY"), criteria.getPositionYMin()));
        }
        if (criteria.getPositionYMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("positionY"), criteria.getPositionYMax()));
        }
        // isEdited - Boolean field
        if (criteria.getIsEdited() != null) {
            predicates.add(cb.equal(root.get("isEdited"), criteria.getIsEdited()));
        }
        // editedAt - LocalDateTime field (supports range)
        if (criteria.getEditedAt() != null) {
            predicates.add(cb.equal(root.get("editedAt"), criteria.getEditedAt()));
        }
        if (criteria.getEditedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("editedAt"), criteria.getEditedAtFrom()));
        }
        if (criteria.getEditedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("editedAt"), criteria.getEditedAtTo()));
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
        // parentComment - ManyToOne relationship
        if (criteria.getParentCommentId() != null) {
            predicates.add(cb.equal(root.get("parentComment").get("id"), criteria.getParentCommentId()));
        }
        if (criteria.getParentCommentRef() != null && !criteria.getParentCommentRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("parentComment").get("ref")),
                "%" + criteria.getParentCommentRef().toLowerCase() + "%"));
        }
        // author - ManyToOne relationship
        if (criteria.getAuthorId() != null) {
            predicates.add(cb.equal(root.get("author").get("id"), criteria.getAuthorId()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
