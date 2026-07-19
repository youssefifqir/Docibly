package com.docibly.dms.dao.criteria.core.folder;

import java.time.LocalDateTime;

public class FolderCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private String name;
    private String nameLike;
    private String description;
    private String descriptionLike;
    private String color;
    private String colorLike;
    private String iconName;
    private String iconNameLike;
    private Boolean isShared;
    private Integer documentCount;
    private Integer documentCountMin;
    private Integer documentCountMax;
    private Long totalSizeBytes;
    private Long totalSizeBytesMin;
    private Long totalSizeBytesMax;

    private Long parentFolderId;
    private String parentFolderRef;
    private Long organizationId;
    private String organizationRef;

    // Constructors
    public FolderCriteria() {
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

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNameLike() { return nameLike; }
    public void setNameLike(String nameLike) { this.nameLike = nameLike; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDescriptionLike() { return descriptionLike; }
    public void setDescriptionLike(String descriptionLike) { this.descriptionLike = descriptionLike; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getColorLike() { return colorLike; }
    public void setColorLike(String colorLike) { this.colorLike = colorLike; }

    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }

    public String getIconNameLike() { return iconNameLike; }
    public void setIconNameLike(String iconNameLike) { this.iconNameLike = iconNameLike; }

    public Boolean getIsShared() { return isShared; }
    public void setIsShared(Boolean isShared) { this.isShared = isShared; }

    public Integer getDocumentCount() { return documentCount; }
    public void setDocumentCount(Integer documentCount) { this.documentCount = documentCount; }

    public Integer getDocumentCountMin() { return documentCountMin; }
    public void setDocumentCountMin(Integer documentCountMin) { this.documentCountMin = documentCountMin; }

    public Integer getDocumentCountMax() { return documentCountMax; }
    public void setDocumentCountMax(Integer documentCountMax) { this.documentCountMax = documentCountMax; }

    public Long getTotalSizeBytes() { return totalSizeBytes; }
    public void setTotalSizeBytes(Long totalSizeBytes) { this.totalSizeBytes = totalSizeBytes; }

    public Long getTotalSizeBytesMin() { return totalSizeBytesMin; }
    public void setTotalSizeBytesMin(Long totalSizeBytesMin) { this.totalSizeBytesMin = totalSizeBytesMin; }

    public Long getTotalSizeBytesMax() { return totalSizeBytesMax; }
    public void setTotalSizeBytesMax(Long totalSizeBytesMax) { this.totalSizeBytesMax = totalSizeBytesMax; }


    public Long getParentFolderId() { return parentFolderId; }
    public void setParentFolderId(Long parentFolderId) { this.parentFolderId = parentFolderId; }

    public String getParentFolderRef() { return parentFolderRef; }
    public void setParentFolderRef(String parentFolderRef) { this.parentFolderRef = parentFolderRef; }

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

        if (name != null && !name.trim().isEmpty()) return false;
        if (nameLike != null && !nameLike.trim().isEmpty()) return false;
        if (description != null && !description.trim().isEmpty()) return false;
        if (descriptionLike != null && !descriptionLike.trim().isEmpty()) return false;
        if (color != null && !color.trim().isEmpty()) return false;
        if (colorLike != null && !colorLike.trim().isEmpty()) return false;
        if (iconName != null && !iconName.trim().isEmpty()) return false;
        if (iconNameLike != null && !iconNameLike.trim().isEmpty()) return false;
        if (isShared != null) return false;
        if (documentCount != null) return false;
        if (documentCountMin != null) return false;
        if (documentCountMax != null) return false;
        if (totalSizeBytes != null) return false;
        if (totalSizeBytesMin != null) return false;
        if (totalSizeBytesMax != null) return false;

        if (parentFolderId != null) return false;
        if (parentFolderRef != null && !parentFolderRef.trim().isEmpty()) return false;
        if (organizationId != null) return false;
        if (organizationRef != null && !organizationRef.trim().isEmpty()) return false;

        return true;
    }
}