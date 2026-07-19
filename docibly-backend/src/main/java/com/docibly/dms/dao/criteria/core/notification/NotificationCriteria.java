package com.docibly.dms.dao.criteria.core.notification;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.NotificationType;

public class NotificationCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private NotificationType type;
    private String title;
    private String titleLike;
    private String message;
    private String messageLike;
    private Boolean read;
    private LocalDateTime readAt;
    private LocalDateTime readAtFrom;
    private LocalDateTime readAtTo;
    private String targetUrl;
    private String targetUrlLike;
    private String relatedEntityType;
    private String relatedEntityTypeLike;
    private String relatedEntityId;
    private String relatedEntityIdLike;

    private String recipientId;

    // Constructors
    public NotificationCriteria() {
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

    public NotificationType getType() { return type; }
    public void setType(NotificationType type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTitleLike() { return titleLike; }
    public void setTitleLike(String titleLike) { this.titleLike = titleLike; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getMessageLike() { return messageLike; }
    public void setMessageLike(String messageLike) { this.messageLike = messageLike; }

    public Boolean getRead() { return read; }
    public void setRead(Boolean read) { this.read = read; }

    public LocalDateTime getReadAt() { return readAt; }
    public void setReadAt(LocalDateTime readAt) { this.readAt = readAt; }

    public LocalDateTime getReadAtFrom() { return readAtFrom; }
    public void setReadAtFrom(LocalDateTime readAtFrom) { this.readAtFrom = readAtFrom; }

    public LocalDateTime getReadAtTo() { return readAtTo; }
    public void setReadAtTo(LocalDateTime readAtTo) { this.readAtTo = readAtTo; }

    public String getTargetUrl() { return targetUrl; }
    public void setTargetUrl(String targetUrl) { this.targetUrl = targetUrl; }

    public String getTargetUrlLike() { return targetUrlLike; }
    public void setTargetUrlLike(String targetUrlLike) { this.targetUrlLike = targetUrlLike; }

    public String getRelatedEntityType() { return relatedEntityType; }
    public void setRelatedEntityType(String relatedEntityType) { this.relatedEntityType = relatedEntityType; }

    public String getRelatedEntityTypeLike() { return relatedEntityTypeLike; }
    public void setRelatedEntityTypeLike(String relatedEntityTypeLike) { this.relatedEntityTypeLike = relatedEntityTypeLike; }

    public String getRelatedEntityId() { return relatedEntityId; }
    public void setRelatedEntityId(String relatedEntityId) { this.relatedEntityId = relatedEntityId; }

    public String getRelatedEntityIdLike() { return relatedEntityIdLike; }
    public void setRelatedEntityIdLike(String relatedEntityIdLike) { this.relatedEntityIdLike = relatedEntityIdLike; }


    public String getRecipientId() { return recipientId; }
    public void setRecipientId(String recipientId) { this.recipientId = recipientId; }


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

        if (type != null) return false;
        if (title != null && !title.trim().isEmpty()) return false;
        if (titleLike != null && !titleLike.trim().isEmpty()) return false;
        if (message != null && !message.trim().isEmpty()) return false;
        if (messageLike != null && !messageLike.trim().isEmpty()) return false;
        if (read != null) return false;
        if (readAt != null) return false;
        if (readAtFrom != null) return false;
        if (readAtTo != null) return false;
        if (targetUrl != null && !targetUrl.trim().isEmpty()) return false;
        if (targetUrlLike != null && !targetUrlLike.trim().isEmpty()) return false;
        if (relatedEntityType != null && !relatedEntityType.trim().isEmpty()) return false;
        if (relatedEntityTypeLike != null && !relatedEntityTypeLike.trim().isEmpty()) return false;
        if (relatedEntityId != null && !relatedEntityId.trim().isEmpty()) return false;
        if (relatedEntityIdLike != null && !relatedEntityIdLike.trim().isEmpty()) return false;

        if (recipientId != null) return false;

        return true;
    }
}