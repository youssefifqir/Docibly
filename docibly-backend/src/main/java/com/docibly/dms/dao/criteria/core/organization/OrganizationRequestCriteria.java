package com.docibly.dms.dao.criteria.core.organization;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.OrgRequestStatus;

public class OrganizationRequestCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private String requestedName;
    private String requestedNameLike;
    private String requestedSlug;
    private String requestedSlugLike;
    private String description;
    private String descriptionLike;
    private String intendedUse;
    private String intendedUseLike;
    private OrgRequestStatus status;
    private LocalDateTime reviewedAt;
    private LocalDateTime reviewedAtFrom;
    private LocalDateTime reviewedAtTo;
    private String rejectionReason;
    private String rejectionReasonLike;
    private Long createdOrganizationId;
    private Long createdOrganizationIdMin;
    private Long createdOrganizationIdMax;

    private String requestedById;
    private String reviewedById;

    // Constructors
    public OrganizationRequestCriteria() {
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

    public String getRequestedName() { return requestedName; }
    public void setRequestedName(String requestedName) { this.requestedName = requestedName; }

    public String getRequestedNameLike() { return requestedNameLike; }
    public void setRequestedNameLike(String requestedNameLike) { this.requestedNameLike = requestedNameLike; }

    public String getRequestedSlug() { return requestedSlug; }
    public void setRequestedSlug(String requestedSlug) { this.requestedSlug = requestedSlug; }

    public String getRequestedSlugLike() { return requestedSlugLike; }
    public void setRequestedSlugLike(String requestedSlugLike) { this.requestedSlugLike = requestedSlugLike; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDescriptionLike() { return descriptionLike; }
    public void setDescriptionLike(String descriptionLike) { this.descriptionLike = descriptionLike; }

    public String getIntendedUse() { return intendedUse; }
    public void setIntendedUse(String intendedUse) { this.intendedUse = intendedUse; }

    public String getIntendedUseLike() { return intendedUseLike; }
    public void setIntendedUseLike(String intendedUseLike) { this.intendedUseLike = intendedUseLike; }

    public OrgRequestStatus getStatus() { return status; }
    public void setStatus(OrgRequestStatus status) { this.status = status; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public LocalDateTime getReviewedAtFrom() { return reviewedAtFrom; }
    public void setReviewedAtFrom(LocalDateTime reviewedAtFrom) { this.reviewedAtFrom = reviewedAtFrom; }

    public LocalDateTime getReviewedAtTo() { return reviewedAtTo; }
    public void setReviewedAtTo(LocalDateTime reviewedAtTo) { this.reviewedAtTo = reviewedAtTo; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getRejectionReasonLike() { return rejectionReasonLike; }
    public void setRejectionReasonLike(String rejectionReasonLike) { this.rejectionReasonLike = rejectionReasonLike; }

    public Long getCreatedOrganizationId() { return createdOrganizationId; }
    public void setCreatedOrganizationId(Long createdOrganizationId) { this.createdOrganizationId = createdOrganizationId; }

    public Long getCreatedOrganizationIdMin() { return createdOrganizationIdMin; }
    public void setCreatedOrganizationIdMin(Long createdOrganizationIdMin) { this.createdOrganizationIdMin = createdOrganizationIdMin; }

    public Long getCreatedOrganizationIdMax() { return createdOrganizationIdMax; }
    public void setCreatedOrganizationIdMax(Long createdOrganizationIdMax) { this.createdOrganizationIdMax = createdOrganizationIdMax; }


    public String getRequestedById() { return requestedById; }
    public void setRequestedById(String requestedById) { this.requestedById = requestedById; }

    public String getReviewedById() { return reviewedById; }
    public void setReviewedById(String reviewedById) { this.reviewedById = reviewedById; }


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

        if (requestedName != null && !requestedName.trim().isEmpty()) return false;
        if (requestedNameLike != null && !requestedNameLike.trim().isEmpty()) return false;
        if (requestedSlug != null && !requestedSlug.trim().isEmpty()) return false;
        if (requestedSlugLike != null && !requestedSlugLike.trim().isEmpty()) return false;
        if (description != null && !description.trim().isEmpty()) return false;
        if (descriptionLike != null && !descriptionLike.trim().isEmpty()) return false;
        if (intendedUse != null && !intendedUse.trim().isEmpty()) return false;
        if (intendedUseLike != null && !intendedUseLike.trim().isEmpty()) return false;
        if (status != null) return false;
        if (reviewedAt != null) return false;
        if (reviewedAtFrom != null) return false;
        if (reviewedAtTo != null) return false;
        if (rejectionReason != null && !rejectionReason.trim().isEmpty()) return false;
        if (rejectionReasonLike != null && !rejectionReasonLike.trim().isEmpty()) return false;
        if (createdOrganizationId != null) return false;
        if (createdOrganizationIdMin != null) return false;
        if (createdOrganizationIdMax != null) return false;

        if (requestedById != null) return false;
        if (reviewedById != null) return false;

        return true;
    }
}