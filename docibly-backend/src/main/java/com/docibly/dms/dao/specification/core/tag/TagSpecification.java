package com.docibly.dms.dao.specification.core.tag;

import com.docibly.dms.dao.criteria.core.tag.TagCriteria;
import com.docibly.dms.bean.core.tag.Tag;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TagSpecification implements Specification<Tag> {

    private final TagCriteria criteria;
    private final boolean distinct;

    public TagSpecification(TagCriteria criteria) {
        this(criteria, false);
    }

    public TagSpecification(TagCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // color - String field (supports like search)
        if (criteria.getColor() != null && !criteria.getColor().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("color")),
                "%" + criteria.getColor().toLowerCase() + "%"));
        }
        if (criteria.getColorLike() != null && !criteria.getColorLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("color")),
                "%" + criteria.getColorLike().toLowerCase() + "%"));
        }
        // usageCount - Integer field (supports range)
        if (criteria.getUsageCount() != null) {
            predicates.add(cb.equal(root.get("usageCount"), criteria.getUsageCount()));
        }
        if (criteria.getUsageCountMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("usageCount"), criteria.getUsageCountMin()));
        }
        if (criteria.getUsageCountMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("usageCount"), criteria.getUsageCountMax()));
        }

        // ── Relationship fields (foreign key lookups) ───────────────
        // organization - ManyToOne relationship
        if (criteria.getOrganizationId() != null) {
            predicates.add(cb.equal(root.get("organization").get("id"), criteria.getOrganizationId()));
        }
        if (criteria.getOrganizationRef() != null && !criteria.getOrganizationRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("organization").get("ref")),
                "%" + criteria.getOrganizationRef().toLowerCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
