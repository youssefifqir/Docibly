package com.docibly.dms.dao.specification.core.document;

import com.docibly.dms.dao.criteria.core.document.DocumentCriteria;
import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.enums.DocumentStatus;
import com.docibly.dms.bean.core.enums.DocumentVisibility;
import com.docibly.dms.bean.core.enums.OcrStatus;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DocumentSpecification implements Specification<Document> {

    private final DocumentCriteria criteria;
    private final boolean distinct;

    public DocumentSpecification(DocumentCriteria criteria) {
        this(criteria, false);
    }

    public DocumentSpecification(DocumentCriteria criteria, boolean distinct) {
        this.criteria = criteria;
        this.distinct = distinct;
    }

    @Override
    public Predicate toPredicate(Root<Document> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
        // title - String field (supports like search)
        if (criteria.getTitle() != null && !criteria.getTitle().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")),
                "%" + criteria.getTitle().toLowerCase() + "%"));
        }
        if (criteria.getTitleLike() != null && !criteria.getTitleLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")),
                "%" + criteria.getTitleLike().toLowerCase() + "%"));
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
        // mimeType - String field (supports like search)
        if (criteria.getMimeType() != null && !criteria.getMimeType().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("mimeType")),
                "%" + criteria.getMimeType().toLowerCase() + "%"));
        }
        if (criteria.getMimeTypeLike() != null && !criteria.getMimeTypeLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("mimeType")),
                "%" + criteria.getMimeTypeLike().toLowerCase() + "%"));
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
        // storageBucket - String field (supports like search)
        if (criteria.getStorageBucket() != null && !criteria.getStorageBucket().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("storageBucket")),
                "%" + criteria.getStorageBucket().toLowerCase() + "%"));
        }
        if (criteria.getStorageBucketLike() != null && !criteria.getStorageBucketLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("storageBucket")),
                "%" + criteria.getStorageBucketLike().toLowerCase() + "%"));
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
        // status - Enum field
        if (criteria.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        }
        // visibility - Enum field
        if (criteria.getVisibility() != null) {
            predicates.add(cb.equal(root.get("visibility"), criteria.getVisibility()));
        }
        // currentVersionNumber - Integer field (supports range)
        if (criteria.getCurrentVersionNumber() != null) {
            predicates.add(cb.equal(root.get("currentVersionNumber"), criteria.getCurrentVersionNumber()));
        }
        if (criteria.getCurrentVersionNumberMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("currentVersionNumber"), criteria.getCurrentVersionNumberMin()));
        }
        if (criteria.getCurrentVersionNumberMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("currentVersionNumber"), criteria.getCurrentVersionNumberMax()));
        }
        // downloadCount - Integer field (supports range)
        if (criteria.getDownloadCount() != null) {
            predicates.add(cb.equal(root.get("downloadCount"), criteria.getDownloadCount()));
        }
        if (criteria.getDownloadCountMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("downloadCount"), criteria.getDownloadCountMin()));
        }
        if (criteria.getDownloadCountMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("downloadCount"), criteria.getDownloadCountMax()));
        }
        // viewCount - Integer field (supports range)
        if (criteria.getViewCount() != null) {
            predicates.add(cb.equal(root.get("viewCount"), criteria.getViewCount()));
        }
        if (criteria.getViewCountMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("viewCount"), criteria.getViewCountMin()));
        }
        if (criteria.getViewCountMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("viewCount"), criteria.getViewCountMax()));
        }
        // isPasswordProtected - Boolean field
        if (criteria.getIsPasswordProtected() != null) {
            predicates.add(cb.equal(root.get("isPasswordProtected"), criteria.getIsPasswordProtected()));
        }
        // passwordHash - String field (supports like search)
        if (criteria.getPasswordHash() != null && !criteria.getPasswordHash().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("passwordHash")),
                "%" + criteria.getPasswordHash().toLowerCase() + "%"));
        }
        if (criteria.getPasswordHashLike() != null && !criteria.getPasswordHashLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("passwordHash")),
                "%" + criteria.getPasswordHashLike().toLowerCase() + "%"));
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
        // archivedAt - LocalDateTime field (supports range)
        if (criteria.getArchivedAt() != null) {
            predicates.add(cb.equal(root.get("archivedAt"), criteria.getArchivedAt()));
        }
        if (criteria.getArchivedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("archivedAt"), criteria.getArchivedAtFrom()));
        }
        if (criteria.getArchivedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("archivedAt"), criteria.getArchivedAtTo()));
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
        // ocrProcessedAt - LocalDateTime field (supports range)
        if (criteria.getOcrProcessedAt() != null) {
            predicates.add(cb.equal(root.get("ocrProcessedAt"), criteria.getOcrProcessedAt()));
        }
        if (criteria.getOcrProcessedAtFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("ocrProcessedAt"), criteria.getOcrProcessedAtFrom()));
        }
        if (criteria.getOcrProcessedAtTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("ocrProcessedAt"), criteria.getOcrProcessedAtTo()));
        }
        // ocrLanguage - String field (supports like search)
        if (criteria.getOcrLanguage() != null && !criteria.getOcrLanguage().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("ocrLanguage")),
                "%" + criteria.getOcrLanguage().toLowerCase() + "%"));
        }
        if (criteria.getOcrLanguageLike() != null && !criteria.getOcrLanguageLike().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("ocrLanguage")),
                "%" + criteria.getOcrLanguageLike().toLowerCase() + "%"));
        }
        // ocrConfidenceScore - BigDecimal field (supports range)
        if (criteria.getOcrConfidenceScore() != null) {
            predicates.add(cb.equal(root.get("ocrConfidenceScore"), criteria.getOcrConfidenceScore()));
        }
        if (criteria.getOcrConfidenceScoreMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("ocrConfidenceScore"), criteria.getOcrConfidenceScoreMin()));
        }
        if (criteria.getOcrConfidenceScoreMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("ocrConfidenceScore"), criteria.getOcrConfidenceScoreMax()));
        }

        // ── Relationship fields (foreign key lookups) ───────────────
        // folder - ManyToOne relationship
        if (criteria.getFolderId() != null) {
            predicates.add(cb.equal(root.get("folder").get("id"), criteria.getFolderId()));
        }
        if (criteria.getFolderRef() != null && !criteria.getFolderRef().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("folder").get("ref")),
                "%" + criteria.getFolderRef().toLowerCase() + "%"));
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
