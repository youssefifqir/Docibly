package com.docibly.dms.dao.criteria.core.tag;

import java.time.LocalDateTime;

public class TagCriteria {

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
    private String slug;
    private String slugLike;
    private String color;
    private String colorLike;
    private Integer usageCount;
    private Integer usageCountMin;
    private Integer usageCountMax;

    private Long organizationId;
    private String organizationRef;

    // Constructors
    public TagCriteria() {
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

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getSlugLike() { return slugLike; }
    public void setSlugLike(String slugLike) { this.slugLike = slugLike; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getColorLike() { return colorLike; }
    public void setColorLike(String colorLike) { this.colorLike = colorLike; }

    public Integer getUsageCount() { return usageCount; }
    public void setUsageCount(Integer usageCount) { this.usageCount = usageCount; }

    public Integer getUsageCountMin() { return usageCountMin; }
    public void setUsageCountMin(Integer usageCountMin) { this.usageCountMin = usageCountMin; }

    public Integer getUsageCountMax() { return usageCountMax; }
    public void setUsageCountMax(Integer usageCountMax) { this.usageCountMax = usageCountMax; }


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
        if (slug != null && !slug.trim().isEmpty()) return false;
        if (slugLike != null && !slugLike.trim().isEmpty()) return false;
        if (color != null && !color.trim().isEmpty()) return false;
        if (colorLike != null && !colorLike.trim().isEmpty()) return false;
        if (usageCount != null) return false;
        if (usageCountMin != null) return false;
        if (usageCountMax != null) return false;

        if (organizationId != null) return false;
        if (organizationRef != null && !organizationRef.trim().isEmpty()) return false;

        return true;
    }
}