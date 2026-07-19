package com.docibly.dms.dao.criteria.core.organization;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.enums.InvitationStatus;

public class OrganizationInvitationCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private String inviteeEmail;
    private String inviteeEmailLike;
    private MemberRole intendedRole;
    private InvitationStatus status;
    private String token;
    private String tokenLike;
    private LocalDateTime expiresAt;
    private LocalDateTime expiresAtFrom;
    private LocalDateTime expiresAtTo;
    private LocalDateTime acceptedAt;
    private LocalDateTime acceptedAtFrom;
    private LocalDateTime acceptedAtTo;

    private Long organizationId;
    private String organizationRef;
    private String invitedById;

    // Constructors
    public OrganizationInvitationCriteria() {
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

    public String getInviteeEmail() { return inviteeEmail; }
    public void setInviteeEmail(String inviteeEmail) { this.inviteeEmail = inviteeEmail; }

    public String getInviteeEmailLike() { return inviteeEmailLike; }
    public void setInviteeEmailLike(String inviteeEmailLike) { this.inviteeEmailLike = inviteeEmailLike; }

    public MemberRole getIntendedRole() { return intendedRole; }
    public void setIntendedRole(MemberRole intendedRole) { this.intendedRole = intendedRole; }

    public InvitationStatus getStatus() { return status; }
    public void setStatus(InvitationStatus status) { this.status = status; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenLike() { return tokenLike; }
    public void setTokenLike(String tokenLike) { this.tokenLike = tokenLike; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getExpiresAtFrom() { return expiresAtFrom; }
    public void setExpiresAtFrom(LocalDateTime expiresAtFrom) { this.expiresAtFrom = expiresAtFrom; }

    public LocalDateTime getExpiresAtTo() { return expiresAtTo; }
    public void setExpiresAtTo(LocalDateTime expiresAtTo) { this.expiresAtTo = expiresAtTo; }

    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }

    public LocalDateTime getAcceptedAtFrom() { return acceptedAtFrom; }
    public void setAcceptedAtFrom(LocalDateTime acceptedAtFrom) { this.acceptedAtFrom = acceptedAtFrom; }

    public LocalDateTime getAcceptedAtTo() { return acceptedAtTo; }
    public void setAcceptedAtTo(LocalDateTime acceptedAtTo) { this.acceptedAtTo = acceptedAtTo; }


    public Long getOrganizationId() { return organizationId; }
    public void setOrganizationId(Long organizationId) { this.organizationId = organizationId; }

    public String getOrganizationRef() { return organizationRef; }
    public void setOrganizationRef(String organizationRef) { this.organizationRef = organizationRef; }

    public String getInvitedById() { return invitedById; }
    public void setInvitedById(String invitedById) { this.invitedById = invitedById; }


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

        if (inviteeEmail != null && !inviteeEmail.trim().isEmpty()) return false;
        if (inviteeEmailLike != null && !inviteeEmailLike.trim().isEmpty()) return false;
        if (intendedRole != null) return false;
        if (status != null) return false;
        if (token != null && !token.trim().isEmpty()) return false;
        if (tokenLike != null && !tokenLike.trim().isEmpty()) return false;
        if (expiresAt != null) return false;
        if (expiresAtFrom != null) return false;
        if (expiresAtTo != null) return false;
        if (acceptedAt != null) return false;
        if (acceptedAtFrom != null) return false;
        if (acceptedAtTo != null) return false;

        if (organizationId != null) return false;
        if (organizationRef != null && !organizationRef.trim().isEmpty()) return false;
        if (invitedById != null) return false;

        return true;
    }
}