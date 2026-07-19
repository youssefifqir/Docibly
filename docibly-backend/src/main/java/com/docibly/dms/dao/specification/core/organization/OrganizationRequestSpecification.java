package com.docibly.dms.dao.specification.core.organization;

import com.docibly.dms.dao.criteria.core.organization.OrganizationRequestCriteria;
import com.docibly.dms.bean.core.organization.OrganizationRequest;
import com.docibly.dms.bean.core.enums.OrgRequestStatus;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrganizationRequestSpecification implements Specification<OrganizationRequest> {

    private final OrganizationRequestCriteria criteria;
    private final boolean distinct;

    public OrganizationRequestSpecification(OrganizationRequestCriteria criteria) {
        this(criteria, false);
    }

    public OrganizationRequestSpecification(OrganizationRequestCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<OrganizationRequest> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // requestedName - String field (supports like search)
        if (criteria.getRequestedName() != null && !criteria.getRequestedName().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("requestedName")),
                "%" + criteria.getRequestedName().toLowerCase() + "%"));
        }
        if (criteria.getRequestedNameLike() != null && !criteria.getRequestedNameLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("requestedName")),
                "%" + criteria.getRequestedNameLike().toLowerCase() + "%"));
        }
        // requestedSlug - String field (supports like search)
        if (criteria.getRequestedSlug() != null && !criteria.getRequestedSlug().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("requestedSlug")),
                "%" + criteria.getRequestedSlug().toLowerCase() + "%"));
        }
        if (criteria.getRequestedSlugLike() != null && !criteria.getRequestedSlugLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("requestedSlug")),
                "%" + criteria.getRequestedSlugLike().toLowerCase() + "%"));
        }
        // description - String field (supports like search)
        if (criteria.getDescription() != null && !criteria.getDescription().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")),
                "%" + criteria.getDescription().toLowerCase() + "%"));
        }
        if (criteria.getDescriptionLike() != null && !criteria.getDescriptionLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")),
                "%" + criteria.getDescriptionLike().toLowerCase() + "%"));
        }
        // intendedUse - String field (supports like search)
        if (criteria.getIntendedUse() != null && !criteria.getIntendedUse().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("intendedUse")),
                "%" + criteria.getIntendedUse().toLowerCase() + "%"));
        }
        if (criteria.getIntendedUseLike() != null && !criteria.getIntendedUseLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("intendedUse")),
                "%" + criteria.getIntendedUseLike().toLowerCase() + "%"));
        }
        // status - Enum field
        if (criteria.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        }
        // reviewedAt - LocalDateTime field (supports range)
        if (criteria.getReviewedAt() != null) {
            predicates.add(cb.equal(root.get("reviewedAt"), criteria.getReviewedAt()));
        }
        if (criteria.getReviewedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("reviewedAt"), criteria.getReviewedAtFrom()));
        }
        if (criteria.getReviewedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("reviewedAt"), criteria.getReviewedAtTo()));
        }
        // rejectionReason - String field (supports like search)
        if (criteria.getRejectionReason() != null && !criteria.getRejectionReason().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("rejectionReason")),
                "%" + criteria.getRejectionReason().toLowerCase() + "%"));
        }
        if (criteria.getRejectionReasonLike() != null && !criteria.getRejectionReasonLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("rejectionReason")),
                "%" + criteria.getRejectionReasonLike().toLowerCase() + "%"));
        }
        // createdOrganizationId - Long field (supports range)
        if (criteria.getCreatedOrganizationId() != null) {
            predicates.add(cb.equal(root.get("createdOrganizationId"), criteria.getCreatedOrganizationId()));
        }
        if (criteria.getCreatedOrganizationIdMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdOrganizationId"), criteria.getCreatedOrganizationIdMin()));
        }
        if (criteria.getCreatedOrganizationIdMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdOrganizationId"), criteria.getCreatedOrganizationIdMax()));
        }

        // ── Relationship fields (foreign key lookups) ───────────────
        // requestedBy - ManyToOne relationship
        if (criteria.getRequestedById() != null) {
            predicates.add(cb.equal(root.get("requestedBy").get("id"), criteria.getRequestedById()));
        }
        // reviewedBy - ManyToOne relationship
        if (criteria.getReviewedById() != null) {
            predicates.add(cb.equal(root.get("reviewedBy").get("id"), criteria.getReviewedById()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
