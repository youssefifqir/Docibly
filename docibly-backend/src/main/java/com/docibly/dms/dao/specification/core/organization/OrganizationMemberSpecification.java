package com.docibly.dms.dao.specification.core.organization;

import com.docibly.dms.dao.criteria.core.organization.OrganizationMemberCriteria;
import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.bean.core.enums.MemberRole;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrganizationMemberSpecification implements Specification<OrganizationMember> {

    private final OrganizationMemberCriteria criteria;
    private final boolean distinct;

    public OrganizationMemberSpecification(OrganizationMemberCriteria criteria) {
        this(criteria, false);
    }

    public OrganizationMemberSpecification(OrganizationMemberCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<OrganizationMember> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // memberRole - Enum field
        if (criteria.getMemberRole() != null) {
            predicates.add(cb.equal(root.get("memberRole"), criteria.getMemberRole()));
        }
        // joinedAt - LocalDateTime field (supports range)
        if (criteria.getJoinedAt() != null) {
            predicates.add(cb.equal(root.get("joinedAt"), criteria.getJoinedAt()));
        }
        if (criteria.getJoinedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("joinedAt"), criteria.getJoinedAtFrom()));
        }
        if (criteria.getJoinedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("joinedAt"), criteria.getJoinedAtTo()));
        }
        // isActive - Boolean field
        if (criteria.getIsActive() != null) {
            predicates.add(cb.equal(root.get("isActive"), criteria.getIsActive()));
        }
        // invitedByEmail - String field (supports like search)
        if (criteria.getInvitedByEmail() != null && !criteria.getInvitedByEmail().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("invitedByEmail")),
                "%" + criteria.getInvitedByEmail().toLowerCase() + "%"));
        }
        if (criteria.getInvitedByEmailLike() != null && !criteria.getInvitedByEmailLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("invitedByEmail")),
                "%" + criteria.getInvitedByEmailLike().toLowerCase() + "%"));
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
        // user - ManyToOne relationship
        if (criteria.getUserId() != null) {
            predicates.add(cb.equal(root.get("user").get("id"), criteria.getUserId()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
