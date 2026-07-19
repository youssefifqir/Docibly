package com.docibly.dms.dao.criteria.core.searchindex;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.DocumentVisibility;

public class SearchIndexCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private Long documentId;
    private Long documentIdMin;
    private Long documentIdMax;
    private String documentTitle;
    private String documentTitleLike;
    private String fullText;
    private String fullTextLike;
    private String ocrText;
    private String ocrTextLike;
    private String tags;
    private String tagsLike;
    private String mimeType;
    private String mimeTypeLike;
    private Long organizationId;
    private Long organizationIdMin;
    private Long organizationIdMax;
    private String ownerId;
    private String ownerIdLike;
    private DocumentVisibility visibility;
    private LocalDateTime indexedAt;
    private LocalDateTime indexedAtFrom;
    private LocalDateTime indexedAtTo;


    // Constructors
    public SearchIndexCriteria() {
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

    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

    public Long getDocumentIdMin() { return documentIdMin; }
    public void setDocumentIdMin(Long documentIdMin) { this.documentIdMin = documentIdMin; }

    public Long getDocumentIdMax() { return documentIdMax; }
    public void setDocumentIdMax(Long documentIdMax) { this.documentIdMax = documentIdMax; }

    public String getDocumentTitle() { return documentTitle; }
    public void setDocumentTitle(String documentTitle) { this.documentTitle = documentTitle; }

    public String getDocumentTitleLike() { return documentTitleLike; }
    public void setDocumentTitleLike(String documentTitleLike) { this.documentTitleLike = documentTitleLike; }

    public String getFullText() { return fullText; }
    public void setFullText(String fullText) { this.fullText = fullText; }

    public String getFullTextLike() { return fullTextLike; }
    public void setFullTextLike(String fullTextLike) { this.fullTextLike = fullTextLike; }

    public String getOcrText() { return ocrText; }
    public void setOcrText(String ocrText) { this.ocrText = ocrText; }

    public String getOcrTextLike() { return ocrTextLike; }
    public void setOcrTextLike(String ocrTextLike) { this.ocrTextLike = ocrTextLike; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getTagsLike() { return tagsLike; }
    public void setTagsLike(String tagsLike) { this.tagsLike = tagsLike; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public String getMimeTypeLike() { return mimeTypeLike; }
    public void setMimeTypeLike(String mimeTypeLike) { this.mimeTypeLike = mimeTypeLike; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public Long getOrganizationIdMin() { return organizationIdMin; }
    public void setOrganizationIdMin(Long organizationIdMin) { this.organizationIdMin = organizationIdMin; }

    public Long getOrganizationIdMax() { return organizationIdMax; }
    public void setOrganizationIdMax(Long organizationIdMax) { this.organizationIdMax = organizationIdMax; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getOwnerIdLike() { return ownerIdLike; }
    public void setOwnerIdLike(String ownerIdLike) { this.ownerIdLike = ownerIdLike; }

    public DocumentVisibility getVisibility() { return visibility; }
    public void setVisibility(DocumentVisibility visibility) { this.visibility = visibility; }

    public LocalDateTime getIndexedAt() { return indexedAt; }
    public void setIndexedAt(LocalDateTime indexedAt) { this.indexedAt = indexedAt; }

    public LocalDateTime getIndexedAtFrom() { return indexedAtFrom; }
    public void setIndexedAtFrom(LocalDateTime indexedAtFrom) { this.indexedAtFrom = indexedAtFrom; }

    public LocalDateTime getIndexedAtTo() { return indexedAtTo; }
    public void setIndexedAtTo(LocalDateTime indexedAtTo) { this.indexedAtTo = indexedAtTo; }



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

        if (documentId != null) return false;
        if (documentIdMin != null) return false;
        if (documentIdMax != null) return false;
        if (documentTitle != null && !documentTitle.trim().isEmpty()) return false;
        if (documentTitleLike != null && !documentTitleLike.trim().isEmpty()) return false;
        if (fullText != null && !fullText.trim().isEmpty()) return false;
        if (fullTextLike != null && !fullTextLike.trim().isEmpty()) return false;
        if (ocrText != null && !ocrText.trim().isEmpty()) return false;
        if (ocrTextLike != null && !ocrTextLike.trim().isEmpty()) return false;
        if (tags != null && !tags.trim().isEmpty()) return false;
        if (tagsLike != null && !tagsLike.trim().isEmpty()) return false;
        if (mimeType != null && !mimeType.trim().isEmpty()) return false;
        if (mimeTypeLike != null && !mimeTypeLike.trim().isEmpty()) return false;
        if (organizationId != null) return false;
        if (organizationIdMin != null) return false;
        if (organizationIdMax != null) return false;
        if (ownerId != null && !ownerId.trim().isEmpty()) return false;
        if (ownerIdLike != null && !ownerIdLike.trim().isEmpty()) return false;
        if (visibility != null) return false;
        if (indexedAt != null) return false;
        if (indexedAtFrom != null) return false;
        if (indexedAtTo != null) return false;


        return true;
    }
}