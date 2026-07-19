package com.docibly.dms.dao.specification.core.organization;

import com.docibly.dms.dao.criteria.core.organization.OrganizationCriteria;
import com.docibly.dms.bean.core.organization.Organization;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrganizationSpecification implements Specification<Organization> {

    private final OrganizationCriteria criteria;
    private final boolean distinct;

    public OrganizationSpecification(OrganizationCriteria criteria) {
        this(criteria, false);
    }

    public OrganizationSpecification(OrganizationCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<Organization> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // name - String field (supports like search)
        if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                "%" + criteria.getName().toLowerCase() + "%"));
        }
        if (criteria.getNameLike() != null && !criteria.getNameLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                "%" + criteria.getNameLike().toLowerCase() + "%"));
        }
        // slug - String field (supports like search)
        if (criteria.getSlug() != null && !criteria.getSlug().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("slug")),
                "%" + criteria.getSlug().toLowerCase() + "%"));
        }
        if (criteria.getSlugLike() != null && !criteria.getSlugLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("slug")),
                "%" + criteria.getSlugLike().toLowerCase() + "%"));
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
        // logoUrl - String field (supports like search)
        if (criteria.getLogoUrl() != null && !criteria.getLogoUrl().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("logoUrl")),
                "%" + criteria.getLogoUrl().toLowerCase() + "%"));
        }
        if (criteria.getLogoUrlLike() != null && !criteria.getLogoUrlLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("logoUrl")),
                "%" + criteria.getLogoUrlLike().toLowerCase() + "%"));
        }
        // website - String field (supports like search)
        if (criteria.getWebsite() != null && !criteria.getWebsite().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("website")),
                "%" + criteria.getWebsite().toLowerCase() + "%"));
        }
        if (criteria.getWebsiteLike() != null && !criteria.getWebsiteLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("website")),
                "%" + criteria.getWebsiteLike().toLowerCase() + "%"));
        }
        // storageUsedBytes - Long field (supports range)
        if (criteria.getStorageUsedBytes() != null) {
            predicates.add(cb.equal(root.get("storageUsedBytes"), criteria.getStorageUsedBytes()));
        }
        if (criteria.getStorageUsedBytesMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("storageUsedBytes"), criteria.getStorageUsedBytesMin()));
        }
        if (criteria.getStorageUsedBytesMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("storageUsedBytes"), criteria.getStorageUsedBytesMax()));
        }
        // storageQuotaBytes - Long field (supports range)
        if (criteria.getStorageQuotaBytes() != null) {
            predicates.add(cb.equal(root.get("storageQuotaBytes"), criteria.getStorageQuotaBytes()));
        }
        if (criteria.getStorageQuotaBytesMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("storageQuotaBytes"), criteria.getStorageQuotaBytesMin()));
        }
        if (criteria.getStorageQuotaBytesMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("storageQuotaBytes"), criteria.getStorageQuotaBytesMax()));
        }
        // maxMembers - Integer field (supports range)
        if (criteria.getMaxMembers() != null) {
            predicates.add(cb.equal(root.get("maxMembers"), criteria.getMaxMembers()));
        }
        if (criteria.getMaxMembersMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("maxMembers"), criteria.getMaxMembersMin()));
        }
        if (criteria.getMaxMembersMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("maxMembers"), criteria.getMaxMembersMax()));
        }
        // isActive - Boolean field
        if (criteria.getIsActive() != null) {
            predicates.add(cb.equal(root.get("isActive"), criteria.getIsActive()));
        }
        // billingEmail - String field (supports like search)
        if (criteria.getBillingEmail() != null && !criteria.getBillingEmail().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("billingEmail")),
                "%" + criteria.getBillingEmail().toLowerCase() + "%"));
        }
        if (criteria.getBillingEmailLike() != null && !criteria.getBillingEmailLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("billingEmail")),
                "%" + criteria.getBillingEmailLike().toLowerCase() + "%"));
        }
        // planTier - String field (supports like search)
        if (criteria.getPlanTier() != null && !criteria.getPlanTier().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("planTier")),
                "%" + criteria.getPlanTier().toLowerCase() + "%"));
        }
        if (criteria.getPlanTierLike() != null && !criteria.getPlanTierLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("planTier")),
                "%" + criteria.getPlanTierLike().toLowerCase() + "%"));
        }
        // trialEndsAt - LocalDateTime field (supports range)
        if (criteria.getTrialEndsAt() != null) {
            predicates.add(cb.equal(root.get("trialEndsAt"), criteria.getTrialEndsAt()));
        }
        if (criteria.getTrialEndsAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("trialEndsAt"), criteria.getTrialEndsAtFrom()));
        }
        if (criteria.getTrialEndsAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("trialEndsAt"), criteria.getTrialEndsAtTo()));
        }

        // ── Relationship fields (foreign key lookups) ───────────────

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
