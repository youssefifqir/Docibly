package com.docibly.dms.dao.criteria.core.auditlog;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.AuditAction;

public class AuditLogCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private AuditAction action;
    private String actorUserId;
    private String actorUserIdLike;
    private String actorEmail;
    private String actorEmailLike;
    private String targetEntityType;
    private String targetEntityTypeLike;
    private String targetEntityId;
    private String targetEntityIdLike;
    private Long organizationId;
    private Long organizationIdMin;
    private Long organizationIdMax;
    private String metadata;
    private String metadataLike;
    private String ipAddress;
    private String ipAddressLike;
    private String userAgent;
    private String userAgentLike;


    // Constructors
    public AuditLogCriteria() {
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

    public AuditAction getAction() { return action; }
    public void setAction(AuditAction action) { this.action = action; }

    public String getActorUserId() { return actorUserId; }
    public void setActorUserId(String actorUserId) { this.actorUserId = actorUserId; }

    public String getActorUserIdLike() { return actorUserIdLike; }
    public void setActorUserIdLike(String actorUserIdLike) { this.actorUserIdLike = actorUserIdLike; }

    public String getActorEmail() { return actorEmail; }
    public void setActorEmail(String actorEmail) { this.actorEmail = actorEmail; }

    public String getActorEmailLike() { return actorEmailLike; }
    public void setActorEmailLike(String actorEmailLike) { this.actorEmailLike = actorEmailLike; }

    public String getTargetEntityType() { return targetEntityType; }
    public void setTargetEntityType(String targetEntityType) { this.targetEntityType = targetEntityType; }

    public String getTargetEntityTypeLike() { return targetEntityTypeLike; }
    public void setTargetEntityTypeLike(String targetEntityTypeLike) { this.targetEntityTypeLike = targetEntityTypeLike; }

    public String getTargetEntityId() { return targetEntityId; }
    public void setTargetEntityId(String targetEntityId) { this.targetEntityId = targetEntityId; }

    public String getTargetEntityIdLike() { return targetEntityIdLike; }
    public void setTargetEntityIdLike(String targetEntityIdLike) { this.targetEntityIdLike = targetEntityIdLike; }

    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public Long getOrganizationIdMin() { return organizationIdMin; }
    public void setOrganizationIdMin(Long organizationIdMin) { this.organizationIdMin = organizationIdMin; }

    public Long getOrganizationIdMax() { return organizationIdMax; }
    public void setOrganizationIdMax(Long organizationIdMax) { this.organizationIdMax = organizationIdMax; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public String getMetadataLike() { return metadataLike; }
    public void setMetadataLike(String metadataLike) { this.metadataLike = metadataLike; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getIpAddressLike() { return ipAddressLike; }
    public void setIpAddressLike(String ipAddressLike) { this.ipAddressLike = ipAddressLike; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getUserAgentLike() { return userAgentLike; }
    public void setUserAgentLike(String userAgentLike) { this.userAgentLike = userAgentLike; }



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

        if (action != null) return false;
        if (actorUserId != null && !actorUserId.trim().isEmpty()) return false;
        if (actorUserIdLike != null && !actorUserIdLike.trim().isEmpty()) return false;
        if (actorEmail != null && !actorEmail.trim().isEmpty()) return false;
        if (actorEmailLike != null && !actorEmailLike.trim().isEmpty()) return false;
        if (targetEntityType != null && !targetEntityType.trim().isEmpty()) return false;
        if (targetEntityTypeLike != null && !targetEntityTypeLike.trim().isEmpty()) return false;
        if (targetEntityId != null && !targetEntityId.trim().isEmpty()) return false;
        if (targetEntityIdLike != null && !targetEntityIdLike.trim().isEmpty()) return false;
        if (organizationId != null) return false;
        if (organizationIdMin != null) return false;
        if (organizationIdMax != null) return false;
        if (metadata != null && !metadata.trim().isEmpty()) return false;
        if (metadataLike != null && !metadataLike.trim().isEmpty()) return false;
        if (ipAddress != null && !ipAddress.trim().isEmpty()) return false;
        if (ipAddressLike != null && !ipAddressLike.trim().isEmpty()) return false;
        if (userAgent != null && !userAgent.trim().isEmpty()) return false;
        if (userAgentLike != null && !userAgentLike.trim().isEmpty()) return false;


        return true;
    }
}