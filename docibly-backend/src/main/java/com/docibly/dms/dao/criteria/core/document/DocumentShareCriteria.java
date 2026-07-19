package com.docibly.dms.dao.criteria.core.document;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.SharePermission;

public class DocumentShareCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private String shareToken;
    private String shareTokenLike;
    private SharePermission permission;
    private String sharedWithEmail;
    private String sharedWithEmailLike;
    private Boolean isPublicLink;
    private LocalDateTime expiresAt;
    private LocalDateTime expiresAtFrom;
    private LocalDateTime expiresAtTo;
    private Boolean isRevoked;
    private LocalDateTime revokedAt;
    private LocalDateTime revokedAtFrom;
    private LocalDateTime revokedAtTo;
    private Integer accessCount;
    private Integer accessCountMin;
    private Integer accessCountMax;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime lastAccessedAtFrom;
    private LocalDateTime lastAccessedAtTo;
    private Boolean requiresPassword;
    private String passwordHash;
    private String passwordHashLike;

    private Long documentId;
    private String documentRef;
    private String sharedById;
    private String sharedWithId;

    // Constructors
    public DocumentShareCriteria() {
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

    public String getShareToken() { return shareToken; }
    public void setShareToken(String shareToken) { this.shareToken = shareToken; }

    public String getShareTokenLike() { return shareTokenLike; }
    public void setShareTokenLike(String shareTokenLike) { this.shareTokenLike = shareTokenLike; }

    public SharePermission getPermission() { return permission; }
    public void setPermission(SharePermission permission) { this.permission = permission; }

    public String getSharedWithEmail() { return sharedWithEmail; }
    public void setSharedWithEmail(String sharedWithEmail) { this.sharedWithEmail = sharedWithEmail; }

    public String getSharedWithEmailLike() { return sharedWithEmailLike; }
    public void setSharedWithEmailLike(String sharedWithEmailLike) { this.sharedWithEmailLike = sharedWithEmailLike; }

    public Boolean getIsPublicLink() { return isPublicLink; }
    public void setIsPublicLink(Boolean isPublicLink) { this.isPublicLink = isPublicLink; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getExpiresAtFrom() { return expiresAtFrom; }
    public void setExpiresAtFrom(LocalDateTime expiresAtFrom) { this.expiresAtFrom = expiresAtFrom; }

    public LocalDateTime getExpiresAtTo() { return expiresAtTo; }
    public void setExpiresAtTo(LocalDateTime expiresAtTo) { this.expiresAtTo = expiresAtTo; }

    public Boolean getIsRevoked() { return isRevoked; }
    public void setIsRevoked(Boolean isRevoked) { this.isRevoked = isRevoked; }

    public LocalDateTime getRevokedAt() { return revokedAt; }
    public void setRevokedAt(LocalDateTime revokedAt) { this.revokedAt = revokedAt; }

    public LocalDateTime getRevokedAtFrom() { return revokedAtFrom; }
    public void setRevokedAtFrom(LocalDateTime revokedAtFrom) { this.revokedAtFrom = revokedAtFrom; }

    public LocalDateTime getRevokedAtTo() { return revokedAtTo; }
    public void setRevokedAtTo(LocalDateTime revokedAtTo) { this.revokedAtTo = revokedAtTo; }

    public Integer getAccessCount() { return accessCount; }
    public void setAccessCount(Integer accessCount) { this.accessCount = accessCount; }

    public Integer getAccessCountMin() { return accessCountMin; }
    public void setAccessCountMin(Integer accessCountMin) { this.accessCountMin = accessCountMin; }

    public Integer getAccessCountMax() { return accessCountMax; }
    public void setAccessCountMax(Integer accessCountMax) { this.accessCountMax = accessCountMax; }

    public LocalDateTime getLastAccessedAt() { return lastAccessedAt; }
    public void setLastAccessedAt(LocalDateTime lastAccessedAt) { this.lastAccessedAt = lastAccessedAt; }

    public LocalDateTime getLastAccessedAtFrom() { return lastAccessedAtFrom; }
    public void setLastAccessedAtFrom(LocalDateTime lastAccessedAtFrom) { this.lastAccessedAtFrom = lastAccessedAtFrom; }

    public LocalDateTime getLastAccessedAtTo() { return lastAccessedAtTo; }
    public void setLastAccessedAtTo(LocalDateTime lastAccessedAtTo) { this.lastAccessedAtTo = lastAccessedAtTo; }

    public Boolean getRequiresPassword() { return requiresPassword; }
    public void setRequiresPassword(Boolean requiresPassword) { this.requiresPassword = requiresPassword; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getPasswordHashLike() { return passwordHashLike; }
    public void setPasswordHashLike(String passwordHashLike) { this.passwordHashLike = passwordHashLike; }


    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

    public String getDocumentRef() { return documentRef; }
    public void setDocumentRef(String documentRef) { this.documentRef = documentRef; }

    public String getSharedById() { return sharedById; }
    public void setSharedById(String sharedById) { this.sharedById = sharedById; }

    public String getSharedWithId() { return sharedWithId; }
    public void setSharedWithId(String sharedWithId) { this.sharedWithId = sharedWithId; }


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

        if (shareToken != null && !shareToken.trim().isEmpty()) return false;
        if (shareTokenLike != null && !shareTokenLike.trim().isEmpty()) return false;
        if (permission != null) return false;
        if (sharedWithEmail != null && !sharedWithEmail.trim().isEmpty()) return false;
        if (sharedWithEmailLike != null && !sharedWithEmailLike.trim().isEmpty()) return false;
        if (isPublicLink != null) return false;
        if (expiresAt != null) return false;
        if (expiresAtFrom != null) return false;
        if (expiresAtTo != null) return false;
        if (isRevoked != null) return false;
        if (revokedAt != null) return false;
        if (revokedAtFrom != null) return false;
        if (revokedAtTo != null) return false;
        if (accessCount != null) return false;
        if (accessCountMin != null) return false;
        if (accessCountMax != null) return false;
        if (lastAccessedAt != null) return false;
        if (lastAccessedAtFrom != null) return false;
        if (lastAccessedAtTo != null) return false;
        if (requiresPassword != null) return false;
        if (passwordHash != null && !passwordHash.trim().isEmpty()) return false;
        if (passwordHashLike != null && !passwordHashLike.trim().isEmpty()) return false;

        if (documentId != null) return false;
        if (documentRef != null && !documentRef.trim().isEmpty()) return false;
        if (sharedById != null) return false;
        if (sharedWithId != null) return false;

        return true;
    }
}