package com.docibly.dms.dao.criteria.core.organization;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.MemberRole;

public class OrganizationMemberCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private MemberRole memberRole;
    private LocalDateTime joinedAt;
    private LocalDateTime joinedAtFrom;
    private LocalDateTime joinedAtTo;
    private Boolean isActive;
    private String invitedByEmail;
    private String invitedByEmailLike;

    private Long organizationId;
    private String organizationRef;
    private String userId;

    // Constructors
    public OrganizationMemberCriteria() {
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

    public MemberRole getMemberRole() { return memberRole; }
    public void setMemberRole(MemberRole memberRole) { this.memberRole = memberRole; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    public LocalDateTime getJoinedAtFrom() { return joinedAtFrom; }
    public void setJoinedAtFrom(LocalDateTime joinedAtFrom) { this.joinedAtFrom = joinedAtFrom; }

    public LocalDateTime getJoinedAtTo() { return joinedAtTo; }
    public void setJoinedAtTo(LocalDateTime joinedAtTo) { this.joinedAtTo = joinedAtTo; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getInvitedByEmail() { return invitedByEmail; }
    public void setInvitedByEmail(String invitedByEmail) { this.invitedByEmail = invitedByEmail; }

    public String getInvitedByEmailLike() { return invitedByEmailLike; }
    public void setInvitedByEmailLike(String invitedByEmailLike) { this.invitedByEmailLike = invitedByEmailLike; }


    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public String getOrganizationRef() { return organizationRef; }
    public void setOrganizationRef(String organizationRef) { this.organizationRef = organizationRef; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }


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

        if (memberRole != null) return false;
        if (joinedAt != null) return false;
        if (joinedAtFrom != null) return false;
        if (joinedAtTo != null) return false;
        if (isActive != null) return false;
        if (invitedByEmail != null && !invitedByEmail.trim().isEmpty()) return false;
        if (invitedByEmailLike != null && !invitedByEmailLike.trim().isEmpty()) return false;

        if (organizationId != null) return false;
        if (organizationRef != null && !organizationRef.trim().isEmpty()) return false;
        if (userId != null) return false;

        return true;
    }
}