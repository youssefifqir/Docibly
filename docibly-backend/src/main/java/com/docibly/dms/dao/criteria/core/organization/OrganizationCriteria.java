package com.docibly.dms.dao.criteria.core.organization;

import java.time.LocalDateTime;

public class OrganizationCriteria {

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
    private String description;
    private String descriptionLike;
    private String logoUrl;
    private String logoUrlLike;
    private String website;
    private String websiteLike;
    private Long storageUsedBytes;
    private Long storageUsedBytesMin;
    private Long storageUsedBytesMax;
    private Long storageQuotaBytes;
    private Long storageQuotaBytesMin;
    private Long storageQuotaBytesMax;
    private Integer maxMembers;
    private Integer maxMembersMin;
    private Integer maxMembersMax;
    private Boolean isActive;
    private String billingEmail;
    private String billingEmailLike;
    private String planTier;
    private String planTierLike;
    private LocalDateTime trialEndsAt;
    private LocalDateTime trialEndsAtFrom;
    private LocalDateTime trialEndsAtTo;


    // Constructors
    public OrganizationCriteria() {
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

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDescriptionLike() { return descriptionLike; }
    public void setDescriptionLike(String descriptionLike) { this.descriptionLike = descriptionLike; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getLogoUrlLike() { return logoUrlLike; }
    public void setLogoUrlLike(String logoUrlLike) { this.logoUrlLike = logoUrlLike; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getWebsiteLike() { return websiteLike; }
    public void setWebsiteLike(String websiteLike) { this.websiteLike = websiteLike; }

    public Long getStorageUsedBytes() { return storageUsedBytes; }
    public void setStorageUsedBytes(Long storageUsedBytes) { this.storageUsedBytes = storageUsedBytes; }

    public Long getStorageUsedBytesMin() { return storageUsedBytesMin; }
    public void setStorageUsedBytesMin(Long storageUsedBytesMin) { this.storageUsedBytesMin = storageUsedBytesMin; }

    public Long getStorageUsedBytesMax() { return storageUsedBytesMax; }
    public void setStorageUsedBytesMax(Long storageUsedBytesMax) { this.storageUsedBytesMax = storageUsedBytesMax; }

    public Long getStorageQuotaBytes() { return storageQuotaBytes; }
    public void setStorageQuotaBytes(Long storageQuotaBytes) { this.storageQuotaBytes = storageQuotaBytes; }

    public Long getStorageQuotaBytesMin() { return storageQuotaBytesMin; }
    public void setStorageQuotaBytesMin(Long storageQuotaBytesMin) { this.storageQuotaBytesMin = storageQuotaBytesMin; }

    public Long getStorageQuotaBytesMax() { return storageQuotaBytesMax; }
    public void setStorageQuotaBytesMax(Long storageQuotaBytesMax) { this.storageQuotaBytesMax = storageQuotaBytesMax; }

    public Integer getMaxMembers() { return maxMembers; }
    public void setMaxMembers(Integer maxMembers) { this.maxMembers = maxMembers; }

    public Integer getMaxMembersMin() { return maxMembersMin; }
    public void setMaxMembersMin(Integer maxMembersMin) { this.maxMembersMin = maxMembersMin; }

    public Integer getMaxMembersMax() { return maxMembersMax; }
    public void setMaxMembersMax(Integer maxMembersMax) { this.maxMembersMax = maxMembersMax; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getBillingEmail() { return billingEmail; }
    public void setBillingEmail(String billingEmail) { this.billingEmail = billingEmail; }

    public String getBillingEmailLike() { return billingEmailLike; }
    public void setBillingEmailLike(String billingEmailLike) { this.billingEmailLike = billingEmailLike; }

    public String getPlanTier() { return planTier; }
    public void setPlanTier(String planTier) { this.planTier = planTier; }

    public String getPlanTierLike() { return planTierLike; }
    public void setPlanTierLike(String planTierLike) { this.planTierLike = planTierLike; }

    public LocalDateTime getTrialEndsAt() { return trialEndsAt; }
    public void setTrialEndsAt(LocalDateTime trialEndsAt) { this.trialEndsAt = trialEndsAt; }

    public LocalDateTime getTrialEndsAtFrom() { return trialEndsAtFrom; }
    public void setTrialEndsAtFrom(LocalDateTime trialEndsAtFrom) { this.trialEndsAtFrom = trialEndsAtFrom; }

    public LocalDateTime getTrialEndsAtTo() { return trialEndsAtTo; }
    public void setTrialEndsAtTo(LocalDateTime trialEndsAtTo) { this.trialEndsAtTo = trialEndsAtTo; }



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
        if (description != null && !description.trim().isEmpty()) return false;
        if (descriptionLike != null && !descriptionLike.trim().isEmpty()) return false;
        if (logoUrl != null && !logoUrl.trim().isEmpty()) return false;
        if (logoUrlLike != null && !logoUrlLike.trim().isEmpty()) return false;
        if (website != null && !website.trim().isEmpty()) return false;
        if (websiteLike != null && !websiteLike.trim().isEmpty()) return false;
        if (storageUsedBytes != null) return false;
        if (storageUsedBytesMin != null) return false;
        if (storageUsedBytesMax != null) return false;
        if (storageQuotaBytes != null) return false;
        if (storageQuotaBytesMin != null) return false;
        if (storageQuotaBytesMax != null) return false;
        if (maxMembers != null) return false;
        if (maxMembersMin != null) return false;
        if (maxMembersMax != null) return false;
        if (isActive != null) return false;
        if (billingEmail != null && !billingEmail.trim().isEmpty()) return false;
        if (billingEmailLike != null && !billingEmailLike.trim().isEmpty()) return false;
        if (planTier != null && !planTier.trim().isEmpty()) return false;
        if (planTierLike != null && !planTierLike.trim().isEmpty()) return false;
        if (trialEndsAt != null) return false;
        if (trialEndsAtFrom != null) return false;
        if (trialEndsAtTo != null) return false;


        return true;
    }
}