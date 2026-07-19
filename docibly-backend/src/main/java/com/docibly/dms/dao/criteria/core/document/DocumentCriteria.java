package com.docibly.dms.dao.criteria.core.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.DocumentStatus;
import com.docibly.dms.bean.core.enums.DocumentVisibility;
import com.docibly.dms.bean.core.enums.OcrStatus;

public class DocumentCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private String title;
    private String titleLike;
    private String description;
    private String descriptionLike;
    private String originalFilename;
    private String originalFilenameLike;
    private String storedFilename;
    private String storedFilenameLike;
    private String mimeType;
    private String mimeTypeLike;
    private Long fileSizeBytes;
    private Long fileSizeBytesMin;
    private Long fileSizeBytesMax;
    private String storageBucket;
    private String storageBucketLike;
    private String storageKey;
    private String storageKeyLike;
    private DocumentStatus status;
    private DocumentVisibility visibility;
    private Integer currentVersionNumber;
    private Integer currentVersionNumberMin;
    private Integer currentVersionNumberMax;
    private Integer downloadCount;
    private Integer downloadCountMin;
    private Integer downloadCountMax;
    private Integer viewCount;
    private Integer viewCountMin;
    private Integer viewCountMax;
    private Boolean isPasswordProtected;
    private String passwordHash;
    private String passwordHashLike;
    private LocalDateTime expiresAt;
    private LocalDateTime expiresAtFrom;
    private LocalDateTime expiresAtTo;
    private LocalDateTime archivedAt;
    private LocalDateTime archivedAtFrom;
    private LocalDateTime archivedAtTo;
    private String checksum;
    private String checksumLike;
    private OcrStatus ocrStatus;
    private String ocrText;
    private String ocrTextLike;
    private LocalDateTime ocrProcessedAt;
    private LocalDateTime ocrProcessedAtFrom;
    private LocalDateTime ocrProcessedAtTo;
    private String ocrLanguage;
    private String ocrLanguageLike;
    private BigDecimal ocrConfidenceScore;
    private BigDecimal ocrConfidenceScoreMin;
    private BigDecimal ocrConfidenceScoreMax;

    private Long folderId;
    private String folderRef;
    private Long organizationId;
    private String organizationRef;

    // Constructors
    public DocumentCriteria() {
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCreatedAtFrom() { return createdAtFrom; }
    public void setCreatedAtFrom(LocalDateTime createdAtFrom) { this.createdAtFrom = createdAtFrom; }

    public LocalDateTime getCreatedAtTo() { return createdAtTo; }
    public void setCreatedAtTo(LocalDateTime createdAtTo) { this.createdAtTo = createdAtTo; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getUpdatedAtFrom() { return updatedAtFrom; }
    public void setUpdatedAtFrom(LocalDateTime updatedAtFrom) { this.updatedAtFrom = updatedAtFrom; }

    public LocalDateTime getUpdatedAtTo() { return updatedAtTo; }
    public void setUpdatedAtTo(LocalDateTime updatedAtTo) { this.updatedAtTo = updatedAtTo; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTitleLike() { return titleLike; }
    public void setTitleLike(String titleLike) { this.titleLike = titleLike; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDescriptionLike() { return descriptionLike; }
    public void setDescriptionLike(String descriptionLike) { this.descriptionLike = descriptionLike; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public String getOriginalFilenameLike() { return originalFilenameLike; }
    public void setOriginalFilenameLike(String originalFilenameLike) { this.originalFilenameLike = originalFilenameLike; }

    public String getStoredFilename() { return storedFilename; }
    public void setStoredFilename(String storedFilename) { this.storedFilename = storedFilename; }

    public String getStoredFilenameLike() { return storedFilenameLike; }
    public void setStoredFilenameLike(String storedFilenameLike) { this.storedFilenameLike = storedFilenameLike; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public String getMimeTypeLike() { return mimeTypeLike; }
    public void setMimeTypeLike(String mimeTypeLike) { this.mimeTypeLike = mimeTypeLike; }

    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

    public Long getFileSizeBytesMin() { return fileSizeBytesMin; }
    public void setFileSizeBytesMin(Long fileSizeBytesMin) { this.fileSizeBytesMin = fileSizeBytesMin; }

    public Long getFileSizeBytesMax() { return fileSizeBytesMax; }
    public void setFileSizeBytesMax(Long fileSizeBytesMax) { this.fileSizeBytesMax = fileSizeBytesMax; }

    public String getStorageBucket() { return storageBucket; }
    public void setStorageBucket(String storageBucket) { this.storageBucket = storageBucket; }

    public String getStorageBucketLike() { return storageBucketLike; }
    public void setStorageBucketLike(String storageBucketLike) { this.storageBucketLike = storageBucketLike; }

    public String getStorageKey() { return storageKey; }
    public void setStorageKey(String storageKey) { this.storageKey = storageKey; }

    public String getStorageKeyLike() { return storageKeyLike; }
    public void setStorageKeyLike(String storageKeyLike) { this.storageKeyLike = storageKeyLike; }

    public DocumentStatus getStatus() { return status; }
    public void setStatus(DocumentStatus status) { this.status = status; }

    public DocumentVisibility getVisibility() { return visibility; }
    public void setVisibility(DocumentVisibility visibility) { this.visibility = visibility; }

    public Integer getCurrentVersionNumber() { return currentVersionNumber; }
    public void setCurrentVersionNumber(Integer currentVersionNumber) { this.currentVersionNumber = currentVersionNumber; }

    public Integer getCurrentVersionNumberMin() { return currentVersionNumberMin; }
    public void setCurrentVersionNumberMin(Integer currentVersionNumberMin) { this.currentVersionNumberMin = currentVersionNumberMin; }

    public Integer getCurrentVersionNumberMax() { return currentVersionNumberMax; }
    public void setCurrentVersionNumberMax(Integer currentVersionNumberMax) { this.currentVersionNumberMax = currentVersionNumberMax; }

    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }

    public Integer getDownloadCountMin() { return downloadCountMin; }
    public void setDownloadCountMin(Integer downloadCountMin) { this.downloadCountMin = downloadCountMin; }

    public Integer getDownloadCountMax() { return downloadCountMax; }
    public void setDownloadCountMax(Integer downloadCountMax) { this.downloadCountMax = downloadCountMax; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Integer getViewCountMin() { return viewCountMin; }
    public void setViewCountMin(Integer viewCountMin) { this.viewCountMin = viewCountMin; }

    public Integer getViewCountMax() { return viewCountMax; }
    public void setViewCountMax(Integer viewCountMax) { this.viewCountMax = viewCountMax; }

    public Boolean getIsPasswordProtected() { return isPasswordProtected; }
    public void setIsPasswordProtected(Boolean isPasswordProtected) { this.isPasswordProtected = isPasswordProtected; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPasswordHashLike() { return passwordHashLike; }
    public void setPasswordHashLike(String passwordHashLike) { this.passwordHashLike = passwordHashLike; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getExpiresAtFrom() { return expiresAtFrom; }
    public void setExpiresAtFrom(LocalDateTime expiresAtFrom) { this.expiresAtFrom = expiresAtFrom; }

    public LocalDateTime getExpiresAtTo() { return expiresAtTo; }
    public void setExpiresAtTo(LocalDateTime expiresAtTo) { this.expiresAtTo = expiresAtTo; }

    public LocalDateTime getArchivedAt() { return archivedAt; }
    public void setArchivedAt(LocalDateTime archivedAt) { this.archivedAt = archivedAt; }

    public LocalDateTime getArchivedAtFrom() { return archivedAtFrom; }
    public void setArchivedAtFrom(LocalDateTime archivedAtFrom) { this.archivedAtFrom = archivedAtFrom; }

    public LocalDateTime getArchivedAtTo() { return archivedAtTo; }
    public void setArchivedAtTo(LocalDateTime archivedAtTo) { this.archivedAtTo = archivedAtTo; }

    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }

    public String getChecksumLike() { return checksumLike; }
    public void setChecksumLike(String checksumLike) { this.checksumLike = checksumLike; }

    public OcrStatus getOcrStatus() { return ocrStatus; }
    public void setOcrStatus(OcrStatus ocrStatus) { this.ocrStatus = ocrStatus; }

    public String getOcrText() { return ocrText; }
    public void setOcrText(String ocrText) { this.ocrText = ocrText; }

    public String getOcrTextLike() { return ocrTextLike; }
    public void setOcrTextLike(String ocrTextLike) { this.ocrTextLike = ocrTextLike; }

    public LocalDateTime getOcrProcessedAt() { return ocrProcessedAt; }
    public void setOcrProcessedAt(LocalDateTime ocrProcessedAt) { this.ocrProcessedAt = ocrProcessedAt; }

    public LocalDateTime getOcrProcessedAtFrom() { return ocrProcessedAtFrom; }
    public void setOcrProcessedAtFrom(LocalDateTime ocrProcessedAtFrom) { this.ocrProcessedAtFrom = ocrProcessedAtFrom; }

    public LocalDateTime getOcrProcessedAtTo() { return ocrProcessedAtTo; }
    public void setOcrProcessedAtTo(LocalDateTime ocrProcessedAtTo) { this.ocrProcessedAtTo = ocrProcessedAtTo; }

    public String getOcrLanguage() { return ocrLanguage; }
    public void setOcrLanguage(String ocrLanguage) { this.ocrLanguage = ocrLanguage; }

    public String getOcrLanguageLike() { return ocrLanguageLike; }
    public void setOcrLanguageLike(String ocrLanguageLike) { this.ocrLanguageLike = ocrLanguageLike; }

    public BigDecimal getOcrConfidenceScore() { return ocrConfidenceScore; }
    public void setOcrConfidenceScore(BigDecimal ocrConfidenceScore) { this.ocrConfidenceScore = ocrConfidenceScore; }

    public BigDecimal getOcrConfidenceScoreMin() { return ocrConfidenceScoreMin; }
    public void setOcrConfidenceScoreMin(BigDecimal ocrConfidenceScoreMin) { this.ocrConfidenceScoreMin = ocrConfidenceScoreMin; }

    public BigDecimal getOcrConfidenceScoreMax() { return ocrConfidenceScoreMax; }
    public void setOcrConfidenceScoreMax(BigDecimal ocrConfidenceScoreMax) { this.ocrConfidenceScoreMax = ocrConfidenceScoreMax; }


    public Long getFolderId() { return folderId; }
    public void setFolderId(Long folderId) { this.folderId = folderId; }

    public String getFolderRef() { return folderRef; }
    public void setFolderRef(String folderRef) { this.folderRef = folderRef; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public String getOrganizationRef() { return organizationRef; }
    public void setOrganizationRef(String organizationRef) { this.organizationRef = organizationRef; }


    // Utility method to check if criteria is empty
    public boolean isEmpty() {
        if (id != null) return false;
        if (ref != null && !ref.trim().isEmpty()) return false;
        if (createdAt != null) return false;
        if (createdAtFrom != null) return false;
        if (createdAtTo != null) return false;
        if (updatedAt != null) return false;
        if (updatedAtFrom != null) return false;
        if (updatedAtTo != null) return false;

        if (title != null && !title.trim().isEmpty()) return false;
        if (titleLike != null && !titleLike.trim().isEmpty()) return false;
        if (description != null && !description.trim().isEmpty()) return false;
        if (descriptionLike != null && !descriptionLike.trim().isEmpty()) return false;
        if (originalFilename != null && !originalFilename.trim().isEmpty()) return false;
        if (originalFilenameLike != null && !originalFilenameLike.trim().isEmpty()) return false;
        if (storedFilename != null && !storedFilename.trim().isEmpty()) return false;
        if (storedFilenameLike != null && !storedFilenameLike.trim().isEmpty()) return false;
        if (mimeType != null && !mimeType.trim().isEmpty()) return false;
        if (mimeTypeLike != null && !mimeTypeLike.trim().isEmpty()) return false;
        if (fileSizeBytes != null) return false;
        if (fileSizeBytesMin != null) return false;
        if (fileSizeBytesMax != null) return false;
        if (storageBucket != null && !storageBucket.trim().isEmpty()) return false;
        if (storageBucketLike != null && !storageBucketLike.trim().isEmpty()) return false;
        if (storageKey != null && !storageKey.trim().isEmpty()) return false;
        if (storageKeyLike != null && !storageKeyLike.trim().isEmpty()) return false;
        if (status != null) return false;
        if (visibility != null) return false;
        if (currentVersionNumber != null) return false;
        if (currentVersionNumberMin != null) return false;
        if (currentVersionNumberMax != null) return false;
        if (downloadCount != null) return false;
        if (downloadCountMin != null) return false;
        if (downloadCountMax != null) return false;
        if (viewCount != null) return false;
        if (viewCountMin != null) return false;
        if (viewCountMax != null) return false;
        if (isPasswordProtected != null) return false;
        if (passwordHash != null && !passwordHash.trim().isEmpty()) return false;
        if (passwordHashLike != null && !passwordHashLike.trim().isEmpty()) return false;
        if (expiresAt != null) return false;
        if (expiresAtFrom != null) return false;
        if (expiresAtTo != null) return false;
        if (archivedAt != null) return false;
        if (archivedAtFrom != null) return false;
        if (archivedAtTo != null) return false;
        if (checksum != null && !checksum.trim().isEmpty()) return false;
        if (checksumLike != null && !checksumLike.trim().isEmpty()) return false;
        if (ocrStatus != null) return false;
        if (ocrText != null && !ocrText.trim().isEmpty()) return false;
        if (ocrTextLike != null && !ocrTextLike.trim().isEmpty()) return false;
        if (ocrProcessedAt != null) return false;
        if (ocrProcessedAtFrom != null) return false;
        if (ocrProcessedAtTo != null) return false;
        if (ocrLanguage != null && !ocrLanguage.trim().isEmpty()) return false;
        if (ocrLanguageLike != null && !ocrLanguageLike.trim().isEmpty()) return false;
        if (ocrConfidenceScore != null) return false;
        if (ocrConfidenceScoreMin != null) return false;
        if (ocrConfidenceScoreMax != null) return false;

        if (folderId != null) return false;
        if (folderRef != null && !folderRef.trim().isEmpty()) return false;
        if (organizationId != null) return false;
        if (organizationRef != null && !organizationRef.trim().isEmpty()) return false;

        return true;
    }
}