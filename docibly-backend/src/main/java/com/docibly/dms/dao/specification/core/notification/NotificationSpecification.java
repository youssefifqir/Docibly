package com.docibly.dms.dao.specification.core.notification;

import com.docibly.dms.dao.criteria.core.notification.NotificationCriteria;
import com.docibly.dms.bean.core.notification.Notification;
import com.docibly.dms.bean.core.enums.NotificationType;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class NotificationSpecification implements Specification<Notification> {

    private final NotificationCriteria criteria;
    private final boolean distinct;

    public NotificationSpecification(NotificationCriteria criteria) {
        this(criteria, false);
    }

    public NotificationSpecification(NotificationCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<Notification> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // type - Enum field
        if (criteria.getType() != null) {
            predicates.add(cb.equal(root.get("type"), criteria.getType()));
        }
        // title - String field (supports like search)
        if (criteria.getTitle() != null && !criteria.getTitle().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")),
                "%" + criteria.getTitle().toLowerCase() + "%"));
        }
        if (criteria.getTitleLike() != null && !criteria.getTitleLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")),
                "%" + criteria.getTitleLike().toLowerCase() + "%"));
        }
        // message - String field (supports like search)
        if (criteria.getMessage() != null && !criteria.getMessage().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("message")),
                "%" + criteria.getMessage().toLowerCase() + "%"));
        }
        if (criteria.getMessageLike() != null && !criteria.getMessageLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("message")),
                "%" + criteria.getMessageLike().toLowerCase() + "%"));
        }
        // read - Boolean field
        if (criteria.getRead() != null) {
            predicates.add(cb.equal(root.get("read"), criteria.getRead()));
        }
        // readAt - LocalDateTime field (supports range)
        if (criteria.getReadAt() != null) {
            predicates.add(cb.equal(root.get("readAt"), criteria.getReadAt()));
        }
        if (criteria.getReadAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("readAt"), criteria.getReadAtFrom()));
        }
        if (criteria.getReadAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("readAt"), criteria.getReadAtTo()));
        }
        // targetUrl - String field (supports like search)
        if (criteria.getTargetUrl() != null && !criteria.getTargetUrl().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("targetUrl")),
                "%" + criteria.getTargetUrl().toLowerCase() + "%"));
        }
        if (criteria.getTargetUrlLike() != null && !criteria.getTargetUrlLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("targetUrl")),
                "%" + criteria.getTargetUrlLike().toLowerCase() + "%"));
        }
        // relatedEntityType - String field (supports like search)
        if (criteria.getRelatedEntityType() != null && !criteria.getRelatedEntityType().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("relatedEntityType")),
                "%" + criteria.getRelatedEntityType().toLowerCase() + "%"));
        }
        if (criteria.getRelatedEntityTypeLike() != null && !criteria.getRelatedEntityTypeLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("relatedEntityType")),
                "%" + criteria.getRelatedEntityTypeLike().toLowerCase() + "%"));
        }
        // relatedEntityId - String field (supports like search)
        if (criteria.getRelatedEntityId() != null && !criteria.getRelatedEntityId().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("relatedEntityId")),
                "%" + criteria.getRelatedEntityId().toLowerCase() + "%"));
        }
        if (criteria.getRelatedEntityIdLike() != null && !criteria.getRelatedEntityIdLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("relatedEntityId")),
                "%" + criteria.getRelatedEntityIdLike().toLowerCase() + "%"));
        }

        // ── Relationship fields (foreign key lookups) ───────────────
        // recipient - ManyToOne relationship
        if (criteria.getRecipientId() != null) {
            predicates.add(cb.equal(root.get("recipient").get("id"), criteria.getRecipientId()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
