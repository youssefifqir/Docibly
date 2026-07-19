package com.docibly.dms.dao.criteria.core.document;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.OcrStatus;

public class DocumentVersionCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private Integer versionNumber;
    private Integer versionNumberMin;
    private Integer versionNumberMax;
    private String label;
    private String labelLike;
    private String changeNote;
    private String changeNoteLike;
    private String originalFilename;
    private String originalFilenameLike;
    private String storedFilename;
    private String storedFilenameLike;
    private String storageKey;
    private String storageKeyLike;
    private Long fileSizeBytes;
    private Long fileSizeBytesMin;
    private Long fileSizeBytesMax;
    private String mimeType;
    private String mimeTypeLike;
    private String checksum;
    private String checksumLike;
    private Boolean isCurrentVersion;
    private OcrStatus ocrStatus;
    private String ocrText;
    private String ocrTextLike;

    private Long documentId;
    private String documentRef;
    private String uploadedById;

    // Constructors
    public DocumentVersionCriteria() {
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

    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }

    public Integer getVersionNumberMin() { return versionNumberMin; }
    public void setVersionNumberMin(Integer versionNumberMin) { this.versionNumberMin = versionNumberMin; }

    public Integer getVersionNumberMax() { return versionNumberMax; }
    public void setVersionNumberMax(Integer versionNumberMax) { this.versionNumberMax = versionNumberMax; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getLabelLike() { return labelLike; }
    public void setLabelLike(String labelLike) { this.labelLike = labelLike; }

    public String getChangeNote() { return changeNote; }
    public void setChangeNote(String changeNote) { this.changeNote = changeNote; }

    public String getChangeNoteLike() { return changeNoteLike; }
    public void setChangeNoteLike(String changeNoteLike) { this.changeNoteLike = changeNoteLike; }

    public String getOriginalFilename() { return originalFilename; }
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    public String getOriginalFilenameLike() { return originalFilenameLike; }
    public void setOriginalFilenameLike(String originalFilenameLike) { this.originalFilenameLike = originalFilenameLike; }

    public String getStoredFilename() { return storedFilename; }
    public void setStoredFilename(String storedFilename) { this.storedFilename = storedFilename; }

    public String getStoredFilenameLike() { return storedFilenameLike; }
    public void setStoredFilenameLike(String storedFilenameLike) { this.storedFilenameLike = storedFilenameLike; }

    public String getStorageKey() { return storageKey; }
    public void setStorageKey(String storageKey) { this.storageKey = storageKey; }

    public String getStorageKeyLike() { return storageKeyLike; }
    public void setStorageKeyLike(String storageKeyLike) { this.storageKeyLike = storageKeyLike; }

    public Long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(Long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

    public Long getFileSizeBytesMin() { return fileSizeBytesMin; }
    public void setFileSizeBytesMin(Long fileSizeBytesMin) { this.fileSizeBytesMin = fileSizeBytesMin; }

    public Long getFileSizeBytesMax() { return fileSizeBytesMax; }
    public void setFileSizeBytesMax(Long fileSizeBytesMax) { this.fileSizeBytesMax = fileSizeBytesMax; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public String getMimeTypeLike() { return mimeTypeLike; }
    public void setMimeTypeLike(String mimeTypeLike) { this.mimeTypeLike = mimeTypeLike; }

    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }

    public String getChecksumLike() { return checksumLike; }
    public void setChecksumLike(String checksumLike) { this.checksumLike = checksumLike; }

    public Boolean getIsCurrentVersion() { return isCurrentVersion; }
    public void setIsCurrentVersion(Boolean isCurrentVersion) { this.isCurrentVersion = isCurrentVersion; }

    public OcrStatus getOcrStatus() { return ocrStatus; }
    public void setOcrStatus(OcrStatus ocrStatus) { this.ocrStatus = ocrStatus; }

    public String getOcrText() { return ocrText; }
    public void setOcrText(String ocrText) { this.ocrText = ocrText; }

    public String getOcrTextLike() { return ocrTextLike; }
    public void setOcrTextLike(String ocrTextLike) { this.ocrTextLike = ocrTextLike; }


    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

    public String getDocumentRef() { return documentRef; }
    public void setDocumentRef(String documentRef) { this.documentRef = documentRef; }

    public String getUploadedById() { return uploadedById; }
    public void setUploadedById(String uploadedById) { this.uploadedById = uploadedById; }


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

        if (versionNumber != null) return false;
        if (versionNumberMin != null) return false;
        if (versionNumberMax != null) return false;
        if (label != null && !label.trim().isEmpty()) return false;
        if (labelLike != null && !labelLike.trim().isEmpty()) return false;
        if (changeNote != null && !changeNote.trim().isEmpty()) return false;
        if (changeNoteLike != null && !changeNoteLike.trim().isEmpty()) return false;
        if (originalFilename != null && !originalFilename.trim().isEmpty()) return false;
        if (originalFilenameLike != null && !originalFilenameLike.trim().isEmpty()) return false;
        if (storedFilename != null && !storedFilename.trim().isEmpty()) return false;
        if (storedFilenameLike != null && !storedFilenameLike.trim().isEmpty()) return false;
        if (storageKey != null && !storageKey.trim().isEmpty()) return false;
        if (storageKeyLike != null && !storageKeyLike.trim().isEmpty()) return false;
        if (fileSizeBytes != null) return false;
        if (fileSizeBytesMin != null) return false;
        if (fileSizeBytesMax != null) return false;
        if (mimeType != null && !mimeType.trim().isEmpty()) return false;
        if (mimeTypeLike != null && !mimeTypeLike.trim().isEmpty()) return false;
        if (checksum != null && !checksum.trim().isEmpty()) return false;
        if (checksumLike != null && !checksumLike.trim().isEmpty()) return false;
        if (isCurrentVersion != null) return false;
        if (ocrStatus != null) return false;
        if (ocrText != null && !ocrText.trim().isEmpty()) return false;
        if (ocrTextLike != null && !ocrTextLike.trim().isEmpty()) return false;

        if (documentId != null) return false;
        if (documentRef != null && !documentRef.trim().isEmpty()) return false;
        if (uploadedById != null) return false;

        return true;
    }
}