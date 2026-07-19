package com.docibly.dms.ws.converter.document.user;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.document.DocumentShare;
import com.docibly.dms.ws.dto.document.user.request.CreateDocumentShareRequest;
import com.docibly.dms.ws.dto.document.user.request.UpdateDocumentShareRequest;
import com.docibly.dms.ws.dto.document.user.response.DocumentShareUserDto;

/**
 * Converter for DocumentShare entity - User view.
 * Access: USER
 */
@Component
public class DocumentShareUserConverter {

    public DocumentShareUserDto toDto(DocumentShare entity) {
        if (entity == null) return null;
        DocumentShareUserDto dto = new DocumentShareUserDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setPermission(entity.getPermission());
        dto.setSharedWithEmail(entity.getSharedWithEmail());
        dto.setIsPublicLink(entity.getIsPublicLink());
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setIsRevoked(entity.getIsRevoked());
        dto.setAccessCount(entity.getAccessCount());
        dto.setLastAccessedAt(entity.getLastAccessedAt());
        dto.setRequiresPassword(entity.getRequiresPassword());
        return dto;
    }

    public DocumentShare toEntity(CreateDocumentShareRequest request) {
        if (request == null) return null;
        DocumentShare entity = new DocumentShare();
        entity.setPermission(request.getPermission());
        entity.setSharedWithEmail(request.getSharedWithEmail());
        entity.setIsPublicLink(request.getIsPublicLink());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setIsRevoked(request.getIsRevoked());
        entity.setAccessCount(request.getAccessCount());
        entity.setLastAccessedAt(request.getLastAccessedAt());
        entity.setRequiresPassword(request.getRequiresPassword());
        return entity;
    }

    public DocumentShare toEntity(UpdateDocumentShareRequest request) {
        if (request == null) return null;
        DocumentShare entity = new DocumentShare();
        entity.setPermission(request.getPermission());
        entity.setSharedWithEmail(request.getSharedWithEmail());
        entity.setIsPublicLink(request.getIsPublicLink());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setIsRevoked(request.getIsRevoked());
        entity.setAccessCount(request.getAccessCount());
        entity.setLastAccessedAt(request.getLastAccessedAt());
        entity.setRequiresPassword(request.getRequiresPassword());
        return entity;
    }
}
