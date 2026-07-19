package com.docibly.dms.dao.specification.core.searchindex;

import com.docibly.dms.dao.criteria.core.searchindex.SearchIndexCriteria;
import com.docibly.dms.bean.core.searchindex.SearchIndex;
import com.docibly.dms.bean.core.enums.DocumentVisibility;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SearchIndexSpecification implements Specification<SearchIndex> {

    private final SearchIndexCriteria criteria;
    private final boolean distinct;

    public SearchIndexSpecification(SearchIndexCriteria criteria) {
        this(criteria, false);
    }

    public SearchIndexSpecification(SearchIndexCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<SearchIndex> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // documentId - Long field (supports range)
        if (criteria.getDocumentId() != null) {
            predicates.add(cb.equal(root.get("documentId"), criteria.getDocumentId()));
        }
        if (criteria.getDocumentIdMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("documentId"), criteria.getDocumentIdMin()));
        }
        if (criteria.getDocumentIdMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("documentId"), criteria.getDocumentIdMax()));
        }
        // documentTitle - String field (supports like search)
        if (criteria.getDocumentTitle() != null && !criteria.getDocumentTitle().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("documentTitle")),
                "%" + criteria.getDocumentTitle().toLowerCase() + "%"));
        }
        if (criteria.getDocumentTitleLike() != null && !criteria.getDocumentTitleLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("documentTitle")),
                "%" + criteria.getDocumentTitleLike().toLowerCase() + "%"));
        }
        // fullText - String field (supports like search)
        if (criteria.getFullText() != null && !criteria.getFullText().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("fullText")),
                "%" + criteria.getFullText().toLowerCase() + "%"));
        }
        if (criteria.getFullTextLike() != null && !criteria.getFullTextLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("fullText")),
                "%" + criteria.getFullTextLike().toLowerCase() + "%"));
        }
        // ocrText - String field (supports like search)
        if (criteria.getOcrText() != null && !criteria.getOcrText().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("ocrText")),
                "%" + criteria.getOcrText().toLowerCase() + "%"));
        }
        if (criteria.getOcrTextLike() != null && !criteria.getOcrTextLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("ocrText")),
                "%" + criteria.getOcrTextLike().toLowerCase() + "%"));
        }
        // tags - String field (supports like search)
        if (criteria.getTags() != null && !criteria.getTags().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("tags")),
                "%" + criteria.getTags().toLowerCase() + "%"));
        }
        if (criteria.getTagsLike() != null && !criteria.getTagsLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("tags")),
                "%" + criteria.getTagsLike().toLowerCase() + "%"));
        }
        // mimeType - String field (supports like search)
        if (criteria.getMimeType() != null && !criteria.getMimeType().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("mimeType")),
                "%" + criteria.getMimeType().toLowerCase() + "%"));
        }
        if (criteria.getMimeTypeLike() != null && !criteria.getMimeTypeLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("mimeType")),
                "%" + criteria.getMimeTypeLike().toLowerCase() + "%"));
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
        // ownerId - String field (supports like search)
        if (criteria.getOwnerId() != null && !criteria.getOwnerId().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("ownerId")),
                "%" + criteria.getOwnerId().toLowerCase() + "%"));
        }
        if (criteria.getOwnerIdLike() != null && !criteria.getOwnerIdLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("ownerId")),
                "%" + criteria.getOwnerIdLike().toLowerCase() + "%"));
        }
        // visibility - Enum field
        if (criteria.getVisibility() != null) {
            predicates.add(cb.equal(root.get("visibility"), criteria.getVisibility()));
        }
        // indexedAt - LocalDateTime field (supports range)
        if (criteria.getIndexedAt() != null) {
            predicates.add(cb.equal(root.get("indexedAt"), criteria.getIndexedAt()));
        }
        if (criteria.getIndexedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("indexedAt"), criteria.getIndexedAtFrom()));
        }
        if (criteria.getIndexedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("indexedAt"), criteria.getIndexedAtTo()));
        }

        // ── Relationship fields (foreign key lookups) ───────────────

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
