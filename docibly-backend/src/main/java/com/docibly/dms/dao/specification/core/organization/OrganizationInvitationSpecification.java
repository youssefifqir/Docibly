package com.docibly.dms.dao.specification.core.organization;

import com.docibly.dms.dao.criteria.core.organization.OrganizationInvitationCriteria;
import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.enums.InvitationStatus;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrganizationInvitationSpecification implements Specification<OrganizationInvitation> {

    private final OrganizationInvitationCriteria criteria;
    private final boolean distinct;

    public OrganizationInvitationSpecification(OrganizationInvitationCriteria criteria) {
        this(criteria, false);
    }

    public OrganizationInvitationSpecification(OrganizationInvitationCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<OrganizationInvitation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // inviteeEmail - String field (supports like search)
        if (criteria.getInviteeEmail() != null && !criteria.getInviteeEmail().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("inviteeEmail")),
                "%" + criteria.getInviteeEmail().toLowerCase() + "%"));
        }
        if (criteria.getInviteeEmailLike() != null && !criteria.getInviteeEmailLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("inviteeEmail")),
                "%" + criteria.getInviteeEmailLike().toLowerCase() + "%"));
        }
        // intendedRole - Enum field
        if (criteria.getIntendedRole() != null) {
            predicates.add(cb.equal(root.get("intendedRole"), criteria.getIntendedRole()));
        }
        // status - Enum field
        if (criteria.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        }
        // token - String field (supports like search)
        if (criteria.getToken() != null && !criteria.getToken().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("token")),
                "%" + criteria.getToken().toLowerCase() + "%"));
        }
        if (criteria.getTokenLike() != null && !criteria.getTokenLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("token")),
                "%" + criteria.getTokenLike().toLowerCase() + "%"));
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
        // acceptedAt - LocalDateTime field (supports range)
        if (criteria.getAcceptedAt() != null) {
            predicates.add(cb.equal(root.get("acceptedAt"), criteria.getAcceptedAt()));
        }
        if (criteria.getAcceptedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("acceptedAt"), criteria.getAcceptedAtFrom()));
        }
        if (criteria.getAcceptedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("acceptedAt"), criteria.getAcceptedAtTo()));
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
        // invitedBy - ManyToOne relationship
        if (criteria.getInvitedById() != null) {
            predicates.add(cb.equal(root.get("invitedBy").get("id"), criteria.getInvitedById()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
