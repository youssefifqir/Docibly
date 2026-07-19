package com.docibly.dms.dao.specification.core.auditlog;

import com.docibly.dms.dao.criteria.core.auditlog.AuditLogCriteria;
import com.docibly.dms.bean.core.auditlog.AuditLog;
import com.docibly.dms.bean.core.enums.AuditAction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AuditLogSpecification implements Specification<AuditLog> {

    private final AuditLogCriteria criteria;
    private final boolean distinct;

    public AuditLogSpecification(AuditLogCriteria criteria) {
        this(criteria, false);
    }

    public AuditLogSpecification(AuditLogCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<AuditLog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // action - Enum field
        if (criteria.getAction() != null) {
            predicates.add(cb.equal(root.get("action"), criteria.getAction()));
        }
        // actorUserId - String field (supports like search)
        if (criteria.getActorUserId() != null && !criteria.getActorUserId().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("actorUserId")),
                "%" + criteria.getActorUserId().toLowerCase() + "%"));
        }
        if (criteria.getActorUserIdLike() != null && !criteria.getActorUserIdLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("actorUserId")),
                "%" + criteria.getActorUserIdLike().toLowerCase() + "%"));
        }
        // actorEmail - String field (supports like search)
        if (criteria.getActorEmail() != null && !criteria.getActorEmail().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("actorEmail")),
                "%" + criteria.getActorEmail().toLowerCase() + "%"));
        }
        if (criteria.getActorEmailLike() != null && !criteria.getActorEmailLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("actorEmail")),
                "%" + criteria.getActorEmailLike().toLowerCase() + "%"));
        }
        // targetEntityType - String field (supports like search)
        if (criteria.getTargetEntityType() != null && !criteria.getTargetEntityType().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("targetEntityType")),
                "%" + criteria.getTargetEntityType().toLowerCase() + "%"));
        }
        if (criteria.getTargetEntityTypeLike() != null && !criteria.getTargetEntityTypeLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("targetEntityType")),
                "%" + criteria.getTargetEntityTypeLike().toLowerCase() + "%"));
        }
        // targetEntityId - String field (supports like search)
        if (criteria.getTargetEntityId() != null && !criteria.getTargetEntityId().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("targetEntityId")),
                "%" + criteria.getTargetEntityId().toLowerCase() + "%"));
        }
        if (criteria.getTargetEntityIdLike() != null && !criteria.getTargetEntityIdLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("targetEntityId")),
                "%" + criteria.getTargetEntityIdLike().toLowerCase() + "%"));
        }
        // organizationId - Long field (supports range)
        if (criteria.getOrganizationId() != null) {
            predicates.add(cb.equal(root.get("organizationId"), criteria.getOrganizationId()));
        }
        if (criteria.getOrganizationIdMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("organizationId"), criteria.getOrganizationIdMin()));
        }
        if (criteria.getOrganizationIdMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("organizationId"), criteria.getOrganizationIdMax()));
        }
        // metadata - String field (supports like search)
        if (criteria.getMetadata() != null && !criteria.getMetadata().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("metadata")),
                "%" + criteria.getMetadata().toLowerCase() + "%"));
        }
        if (criteria.getMetadataLike() != null && !criteria.getMetadataLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("metadata")),
                "%" + criteria.getMetadataLike().toLowerCase() + "%"));
        }
        // ipAddress - String field (supports like search)
        if (criteria.getIpAddress() != null && !criteria.getIpAddress().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("ipAddress")),
                "%" + criteria.getIpAddress().toLowerCase() + "%"));
        }
        if (criteria.getIpAddressLike() != null && !criteria.getIpAddressLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("ipAddress")),
                "%" + criteria.getIpAddressLike().toLowerCase() + "%"));
        }
        // userAgent - String field (supports like search)
        if (criteria.getUserAgent() != null && !criteria.getUserAgent().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("userAgent")),
                "%" + criteria.getUserAgent().toLowerCase() + "%"));
        }
        if (criteria.getUserAgentLike() != null && !criteria.getUserAgentLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("userAgent")),
                "%" + criteria.getUserAgentLike().toLowerCase() + "%"));
        }

        // ── Relationship fields (foreign key lookups) ───────────────

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
