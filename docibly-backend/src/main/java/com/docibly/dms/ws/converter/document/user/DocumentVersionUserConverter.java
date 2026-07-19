package com.docibly.dms.ws.converter.document.user;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.ws.dto.document.user.request.CreateDocumentVersionRequest;
import com.docibly.dms.ws.dto.document.user.request.UpdateDocumentVersionRequest;
import com.docibly.dms.ws.dto.document.user.response.DocumentVersionUserDto;

/**
 * Converter for DocumentVersion entity - User view.
 * Access: USER
 */
@Component
public class DocumentVersionUserConverter {

    public DocumentVersionUserDto toDto(DocumentVersion entity) {
        if (entity == null) return null;
        DocumentVersionUserDto dto = new DocumentVersionUserDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setVersionNumber(entity.getVersionNumber());
        dto.setLabel(entity.getLabel());
        dto.setChangeNote(entity.getChangeNote());
        dto.setOriginalFilename(entity.getOriginalFilename());
        dto.setStoredFilename(entity.getStoredFilename());
        dto.setFileSizeBytes(entity.getFileSizeBytes());
        dto.setMimeType(entity.getMimeType());
        dto.setIsCurrentVersion(entity.getIsCurrentVersion());
        dto.setOcrStatus(entity.getOcrStatus());
        dto.setOcrText(entity.getOcrText());
        return dto;
    }

    public DocumentVersion toEntity(CreateDocumentVersionRequest request) {
        if (request == null) return null;
        DocumentVersion entity = new DocumentVersion();
        entity.setVersionNumber(request.getVersionNumber());
        entity.setLabel(request.getLabel());
        entity.setChangeNote(request.getChangeNote());
        entity.setOriginalFilename(request.getOriginalFilename());
        entity.setStoredFilename(request.getStoredFilename());
        entity.setFileSizeBytes(request.getFileSizeBytes());
        entity.setMimeType(request.getMimeType());
        entity.setIsCurrentVersion(request.getIsCurrentVersion());
        entity.setOcrStatus(request.getOcrStatus());
        entity.setOcrText(request.getOcrText());
        return entity;
    }

    public DocumentVersion toEntity(UpdateDocumentVersionRequest request) {
        if (request == null) return null;
        DocumentVersion entity = new DocumentVersion();
        entity.setVersionNumber(request.getVersionNumber());
        entity.setLabel(request.getLabel());
        entity.setChangeNote(request.getChangeNote());
        entity.setOriginalFilename(request.getOriginalFilename());
        entity.setStoredFilename(request.getStoredFilename());
        entity.setFileSizeBytes(request.getFileSizeBytes());
        entity.setMimeType(request.getMimeType());
        entity.setIsCurrentVersion(request.getIsCurrentVersion());
        entity.setOcrStatus(request.getOcrStatus());
        entity.setOcrText(request.getOcrText());
        return entity;
    }
}
