package com.docibly.dms.ws.converter.document.admin;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.document.DocumentShare;
import com.docibly.dms.ws.dto.document.admin.request.CreateDocumentShareRequest;
import com.docibly.dms.ws.dto.document.admin.request.UpdateDocumentShareRequest;
import com.docibly.dms.ws.dto.document.admin.response.DocumentShareAdminDto;

/**
 * Converter for DocumentShare entity - Admin view.
 * Access: ADMIN
 */
@Component
public class DocumentShareAdminConverter {

    public DocumentShareAdminDto toDto(DocumentShare entity) {
        if (entity == null) return null;
        DocumentShareAdminDto dto = new DocumentShareAdminDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setShareToken(entity.getShareToken());
        dto.setPermission(entity.getPermission());
        dto.setSharedWithEmail(entity.getSharedWithEmail());
        dto.setIsPublicLink(entity.getIsPublicLink());
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setIsRevoked(entity.getIsRevoked());
        dto.setRevokedAt(entity.getRevokedAt());
        dto.setAccessCount(entity.getAccessCount());
        dto.setLastAccessedAt(entity.getLastAccessedAt());
        dto.setRequiresPassword(entity.getRequiresPassword());
        dto.setPasswordHash(entity.getPasswordHash());
        return dto;
    }

    public DocumentShare toEntity(CreateDocumentShareRequest request) {
        if (request == null) return null;
        DocumentShare entity = new DocumentShare();
        entity.setShareToken(request.getShareToken());
        entity.setPermission(request.getPermission());
        entity.setSharedWithEmail(request.getSharedWithEmail());
        entity.setIsPublicLink(request.getIsPublicLink());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setIsRevoked(request.getIsRevoked());
        entity.setRevokedAt(request.getRevokedAt());
        entity.setAccessCount(request.getAccessCount());
        entity.setLastAccessedAt(request.getLastAccessedAt());
        entity.setRequiresPassword(request.getRequiresPassword());
        entity.setPasswordHash(request.getPasswordHash());
        return entity;
    }

    public DocumentShare toEntity(UpdateDocumentShareRequest request) {
        if (request == null) return null;
        DocumentShare entity = new DocumentShare();
        entity.setShareToken(request.getShareToken());
        entity.setPermission(request.getPermission());
        entity.setSharedWithEmail(request.getSharedWithEmail());
        entity.setIsPublicLink(request.getIsPublicLink());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setIsRevoked(request.getIsRevoked());
        entity.setRevokedAt(request.getRevokedAt());
        entity.setAccessCount(request.getAccessCount());
        entity.setLastAccessedAt(request.getLastAccessedAt());
        entity.setRequiresPassword(request.getRequiresPassword());
        entity.setPasswordHash(request.getPasswordHash());
        return entity;
    }
}
