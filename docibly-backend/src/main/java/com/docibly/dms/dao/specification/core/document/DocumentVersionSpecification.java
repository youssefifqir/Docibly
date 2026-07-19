package com.docibly.dms.dao.specification.core.document;

import com.docibly.dms.dao.criteria.core.document.DocumentVersionCriteria;
import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.bean.core.enums.OcrStatus;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DocumentVersionSpecification implements Specification<DocumentVersion> {

    private final DocumentVersionCriteria criteria;
    private final boolean distinct;

    public DocumentVersionSpecification(DocumentVersionCriteria criteria) {
        this(criteria, false);
    }

    public DocumentVersionSpecification(DocumentVersionCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<DocumentVersion> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // versionNumber - Integer field (supports range)
        if (criteria.getVersionNumber() != null) {
            predicates.add(cb.equal(root.get("versionNumber"), criteria.getVersionNumber()));
        }
        if (criteria.getVersionNumberMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("versionNumber"), criteria.getVersionNumberMin()));
        }
        if (criteria.getVersionNumberMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("versionNumber"), criteria.getVersionNumberMax()));
        }
        // label - String field (supports like search)
        if (criteria.getLabel() != null && !criteria.getLabel().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("label")),
                "%" + criteria.getLabel().toLowerCase() + "%"));
        }
        if (criteria.getLabelLike() != null && !criteria.getLabelLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("label")),
                "%" + criteria.getLabelLike().toLowerCase() + "%"));
        }
        // changeNote - String field (supports like search)
        if (criteria.getChangeNote() != null && !criteria.getChangeNote().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("changeNote")),
                "%" + criteria.getChangeNote().toLowerCase() + "%"));
        }
        if (criteria.getChangeNoteLike() != null && !criteria.getChangeNoteLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("changeNote")),
                "%" + criteria.getChangeNoteLike().toLowerCase() + "%"));
        }
        // originalFilename - String field (supports like search)
        if (criteria.getOriginalFilename() != null && !criteria.getOriginalFilename().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("originalFilename")),
                "%" + criteria.getOriginalFilename().toLowerCase() + "%"));
        }
        if (criteria.getOriginalFilenameLike() != null && !criteria.getOriginalFilenameLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("originalFilename")),
                "%" + criteria.getOriginalFilenameLike().toLowerCase() + "%"));
        }
        // storedFilename - String field (supports like search)
        if (criteria.getStoredFilename() != null && !criteria.getStoredFilename().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("storedFilename")),
                "%" + criteria.getStoredFilename().toLowerCase() + "%"));
        }
        if (criteria.getStoredFilenameLike() != null && !criteria.getStoredFilenameLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("storedFilename")),
                "%" + criteria.getStoredFilenameLike().toLowerCase() + "%"));
        }
        // storageKey - String field (supports like search)
        if (criteria.getStorageKey() != null && !criteria.getStorageKey().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("storageKey")),
                "%" + criteria.getStorageKey().toLowerCase() + "%"));
        }
        if (criteria.getStorageKeyLike() != null && !criteria.getStorageKeyLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("storageKey")),
                "%" + criteria.getStorageKeyLike().toLowerCase() + "%"));
        }
        // fileSizeBytes - Long field (supports range)
        if (criteria.getFileSizeBytes() != null) {
            predicates.add(cb.equal(root.get("fileSizeBytes"), criteria.getFileSizeBytes()));
        }
        if (criteria.getFileSizeBytesMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("fileSizeBytes"), criteria.getFileSizeBytesMin()));
        }
        if (criteria.getFileSizeBytesMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("fileSizeBytes"), criteria.getFileSizeBytesMax()));
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
        // checksum - String field (supports like search)
        if (criteria.getChecksum() != null && !criteria.getChecksum().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("checksum")),
                "%" + criteria.getChecksum().toLowerCase() + "%"));
        }
        if (criteria.getChecksumLike() != null && !criteria.getChecksumLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("checksum")),
                "%" + criteria.getChecksumLike().toLowerCase() + "%"));
        }
        // isCurrentVersion - Boolean field
        if (criteria.getIsCurrentVersion() != null) {
            predicates.add(cb.equal(root.get("isCurrentVersion"), criteria.getIsCurrentVersion()));
        }
        // ocrStatus - Enum field
        if (criteria.getOcrStatus() != null) {
            predicates.add(cb.equal(root.get("ocrStatus"), criteria.getOcrStatus()));
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

        // ── Relationship fields (foreign key lookups) ───────────────
        // document - ManyToOne relationship
        if (criteria.getDocumentId() != null) {
            predicates.add(cb.equal(root.get("document").get("id"), criteria.getDocumentId()));
        }
        if (criteria.getDocumentRef() != null && !criteria.getDocumentRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("document").get("ref")),
                "%" + criteria.getDocumentRef().toLowerCase() + "%"));
        }
        // uploadedBy - ManyToOne relationship
        if (criteria.getUploadedById() != null) {
            predicates.add(cb.equal(root.get("uploadedBy").get("id"), criteria.getUploadedById()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
