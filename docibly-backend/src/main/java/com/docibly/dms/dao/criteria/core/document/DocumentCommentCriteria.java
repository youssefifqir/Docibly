package com.docibly.dms.dao.criteria.core.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DocumentCommentCriteria {

    private Long id;
    private String ref;
    private LocalDateTime createdAt;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
    private LocalDateTime updatedAt;
    private LocalDateTime updatedAtFrom;
    private LocalDateTime updatedAtTo;

    private String content;
    private String contentLike;
    private Boolean isResolved;
    private LocalDateTime resolvedAt;
    private LocalDateTime resolvedAtFrom;
    private LocalDateTime resolvedAtTo;
    private Integer pageNumber;
    private Integer pageNumberMin;
    private Integer pageNumberMax;
    private BigDecimal positionX;
    private BigDecimal positionXMin;
    private BigDecimal positionXMax;
    private BigDecimal positionY;
    private BigDecimal positionYMin;
    private BigDecimal positionYMax;
    private Boolean isEdited;
    private LocalDateTime editedAt;
    private LocalDateTime editedAtFrom;
    private LocalDateTime editedAtTo;

    private Long documentId;
    private String documentRef;
    private Long parentCommentId;
    private String parentCommentRef;
    private String authorId;

    // Constructors
    public DocumentCommentCriteria() {
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

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getContentLike() { return contentLike; }
    public void setContentLike(String contentLike) { this.contentLike = contentLike; }

    public Boolean getIsResolved() { return isResolved; }
    public void setIsResolved(Boolean isResolved) { this.isResolved = isResolved; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    public LocalDateTime getResolvedAtFrom() { return resolvedAtFrom; }
    public void setResolvedAtFrom(LocalDateTime resolvedAtFrom) { this.resolvedAtFrom = resolvedAtFrom; }

    public LocalDateTime getResolvedAtTo() { return resolvedAtTo; }
    public void setResolvedAtTo(LocalDateTime resolvedAtTo) { this.resolvedAtTo = resolvedAtTo; }

    public Integer getPageNumber() { return pageNumber; }
    public void setPageNumber(Integer pageNumber) { this.pageNumber = pageNumber; }

    public Integer getPageNumberMin() { return pageNumberMin; }
    public void setPageNumberMin(Integer pageNumberMin) { this.pageNumberMin = pageNumberMin; }

    public Integer getPageNumberMax() { return pageNumberMax; }
    public void setPageNumberMax(Integer pageNumberMax) { this.pageNumberMax = pageNumberMax; }

    public BigDecimal getPositionX() { return positionX; }
    public void setPositionX(BigDecimal positionX) { this.positionX = positionX; }

    public BigDecimal getPositionXMin() { return positionXMin; }
    public void setPositionXMin(BigDecimal positionXMin) { this.positionXMin = positionXMin; }

    public BigDecimal getPositionXMax() { return positionXMax; }
    public void setPositionXMax(BigDecimal positionXMax) { this.positionXMax = positionXMax; }

    public BigDecimal getPositionY() { return positionY; }
    public void setPositionY(BigDecimal positionY) { this.positionY = positionY; }

    public BigDecimal getPositionYMin() { return positionYMin; }
    public void setPositionYMin(BigDecimal positionYMin) { this.positionYMin = positionYMin; }

    public BigDecimal getPositionYMax() { return positionYMax; }
    public void setPositionYMax(BigDecimal positionYMax) { this.positionYMax = positionYMax; }

    public Boolean getIsEdited() { return isEdited; }
    public void setIsEdited(Boolean isEdited) { this.isEdited = isEdited; }

    public LocalDateTime getEditedAt() { return editedAt; }
    public void setEditedAt(LocalDateTime editedAt) { this.editedAt = editedAt; }

    public LocalDateTime getEditedAtFrom() { return editedAtFrom; }
    public void setEditedAtFrom(LocalDateTime editedAtFrom) { this.editedAtFrom = editedAtFrom; }

    public LocalDateTime getEditedAtTo() { return editedAtTo; }
    public void setEditedAtTo(LocalDateTime editedAtTo) { this.editedAtTo = editedAtTo; }


    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

    public String getDocumentRef() { return documentRef; }
    public void setDocumentRef(String documentRef) { this.documentRef = documentRef; }

    public Long getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(Long parentCommentId) { this.parentCommentId = parentCommentId; }

    public String getParentCommentRef() { return parentCommentRef; }
    public void setParentCommentRef(String parentCommentRef) { this.parentCommentRef = parentCommentRef; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }


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

        if (content != null && !content.trim().isEmpty()) return false;
        if (contentLike != null && !contentLike.trim().isEmpty()) return false;
        if (isResolved != null) return false;
        if (resolvedAt != null) return false;
        if (resolvedAtFrom != null) return false;
        if (resolvedAtTo != null) return false;
        if (pageNumber != null) return false;
        if (pageNumberMin != null) return false;
        if (pageNumberMax != null) return false;
        if (positionX != null) return false;
        if (positionXMin != null) return false;
        if (positionXMax != null) return false;
        if (positionY != null) return false;
        if (positionYMin != null) return false;
        if (positionYMax != null) return false;
        if (isEdited != null) return false;
        if (editedAt != null) return false;
        if (editedAtFrom != null) return false;
        if (editedAtTo != null) return false;

        if (documentId != null) return false;
        if (documentRef != null && !documentRef.trim().isEmpty()) return false;
        if (parentCommentId != null) return false;
        if (parentCommentRef != null && !parentCommentRef.trim().isEmpty()) return false;
        if (authorId != null) return false;

        return true;
    }
}