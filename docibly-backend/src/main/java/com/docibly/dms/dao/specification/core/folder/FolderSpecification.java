package com.docibly.dms.dao.specification.core.folder;

import com.docibly.dms.dao.criteria.core.folder.FolderCriteria;
import com.docibly.dms.bean.core.folder.Folder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FolderSpecification implements Specification<Folder> {

    private final FolderCriteria criteria;
    private final boolean distinct;

    public FolderSpecification(FolderCriteria criteria) {
        this(criteria, false);
    }

    public FolderSpecification(FolderCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<Folder> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // description - String field (supports like search)
        if (criteria.getDescription() != null && !criteria.getDescription().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")),
                "%" + criteria.getDescription().toLowerCase() + "%"));
        }
        if (criteria.getDescriptionLike() != null && !criteria.getDescriptionLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")),
                "%" + criteria.getDescriptionLike().toLowerCase() + "%"));
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
        // iconName - String field (supports like search)
        if (criteria.getIconName() != null && !criteria.getIconName().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("iconName")),
                "%" + criteria.getIconName().toLowerCase() + "%"));
        }
        if (criteria.getIconNameLike() != null && !criteria.getIconNameLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("iconName")),
                "%" + criteria.getIconNameLike().toLowerCase() + "%"));
        }
        // isShared - Boolean field
        if (criteria.getIsShared() != null) {
            predicates.add(cb.equal(root.get("isShared"), criteria.getIsShared()));
        }
        // documentCount - Integer field (supports range)
        if (criteria.getDocumentCount() != null) {
            predicates.add(cb.equal(root.get("documentCount"), criteria.getDocumentCount()));
        }
        if (criteria.getDocumentCountMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("documentCount"), criteria.getDocumentCountMin()));
        }
        if (criteria.getDocumentCountMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("documentCount"), criteria.getDocumentCountMax()));
        }
        // totalSizeBytes - Long field (supports range)
        if (criteria.getTotalSizeBytes() != null) {
            predicates.add(cb.equal(root.get("totalSizeBytes"), criteria.getTotalSizeBytes()));
        }
        if (criteria.getTotalSizeBytesMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("totalSizeBytes"), criteria.getTotalSizeBytesMin()));
        }
        if (criteria.getTotalSizeBytesMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("totalSizeBytes"), criteria.getTotalSizeBytesMax()));
        }

        // ── Relationship fields (foreign key lookups) ───────────────
        // parentFolder - ManyToOne relationship
        if (criteria.getParentFolderId() != null) {
            predicates.add(cb.equal(root.get("parentFolder").get("id"), criteria.getParentFolderId()));
        }
        if (criteria.getParentFolderRef() != null && !criteria.getParentFolderRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("parentFolder").get("ref")),
                "%" + criteria.getParentFolderRef().toLowerCase() + "%"));
        }
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
