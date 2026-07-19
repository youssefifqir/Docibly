package com.docibly.dms.ws.converter.document;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.document.DocumentComment;
import com.docibly.dms.ws.dto.document.request.CreateDocumentCommentRequest;
import com.docibly.dms.ws.dto.document.request.UpdateDocumentCommentRequest;
import com.docibly.dms.ws.dto.document.response.DocumentCommentResponse;

@Component
public class DocumentCommentConverter {

    public DocumentCommentResponse toResponse(DocumentComment entity) {
        if (entity == null) return null;
        DocumentCommentResponse response = new DocumentCommentResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setContent(entity.getContent());
        response.setIsResolved(entity.getIsResolved());
        response.setResolvedAt(entity.getResolvedAt());
        response.setPageNumber(entity.getPageNumber());
        response.setPositionX(entity.getPositionX());
        response.setPositionY(entity.getPositionY());
        response.setIsEdited(entity.getIsEdited());
        response.setEditedAt(entity.getEditedAt());
        if (entity.getDocument() != null) {
            response.setDocumentId(entity.getDocument().getId());
            response.setDocumentRef(entity.getDocument().getRef());
        }
        if (entity.getParentComment() != null) {
            response.setParentCommentId(entity.getParentComment().getId());
            response.setParentCommentRef(entity.getParentComment().getRef());
        }
        if (entity.getAuthor() != null) {
            response.setAuthorId(entity.getAuthor().getId());
        }
        return response;
    }

    public DocumentComment toEntity(CreateDocumentCommentRequest request) {
        if (request == null) return null;
        DocumentComment entity = new DocumentComment();
        entity.setContent(request.getContent());
        entity.setIsResolved(request.getIsResolved());
        entity.setResolvedAt(request.getResolvedAt());
        entity.setPageNumber(request.getPageNumber());
        entity.setPositionX(request.getPositionX());
        entity.setPositionY(request.getPositionY());
        entity.setIsEdited(request.getIsEdited());
        entity.setEditedAt(request.getEditedAt());
        return entity;
    }

    public DocumentComment toEntity(UpdateDocumentCommentRequest request) {
        if (request == null) return null;
        DocumentComment entity = new DocumentComment();
        entity.setContent(request.getContent());
        entity.setIsResolved(request.getIsResolved());
        entity.setResolvedAt(request.getResolvedAt());
        entity.setPageNumber(request.getPageNumber());
        entity.setPositionX(request.getPositionX());
        entity.setPositionY(request.getPositionY());
        entity.setIsEdited(request.getIsEdited());
        entity.setEditedAt(request.getEditedAt());
        return entity;
    }
}

