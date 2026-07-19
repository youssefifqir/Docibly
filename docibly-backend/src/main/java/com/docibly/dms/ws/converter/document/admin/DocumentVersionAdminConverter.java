package com.docibly.dms.ws.converter.document.admin;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.ws.dto.document.admin.request.CreateDocumentVersionRequest;
import com.docibly.dms.ws.dto.document.admin.request.UpdateDocumentVersionRequest;
import com.docibly.dms.ws.dto.document.admin.response.DocumentVersionAdminDto;

/**
 * Converter for DocumentVersion entity - Admin view.
 * Access: ADMIN
 */
@Component
public class DocumentVersionAdminConverter {

    public DocumentVersionAdminDto toDto(DocumentVersion entity) {
        if (entity == null) return null;
        DocumentVersionAdminDto dto = new DocumentVersionAdminDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setVersionNumber(entity.getVersionNumber());
        dto.setLabel(entity.getLabel());
        dto.setChangeNote(entity.getChangeNote());
        dto.setOriginalFilename(entity.getOriginalFilename());
        dto.setStoredFilename(entity.getStoredFilename());
        dto.setStorageKey(entity.getStorageKey());
        dto.setFileSizeBytes(entity.getFileSizeBytes());
        dto.setMimeType(entity.getMimeType());
        dto.setChecksum(entity.getChecksum());
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
        entity.setStorageKey(request.getStorageKey());
        entity.setFileSizeBytes(request.getFileSizeBytes());
        entity.setMimeType(request.getMimeType());
        entity.setChecksum(request.getChecksum());
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
        entity.setStorageKey(request.getStorageKey());
        entity.setFileSizeBytes(request.getFileSizeBytes());
        entity.setMimeType(request.getMimeType());
        entity.setChecksum(request.getChecksum());
        entity.setIsCurrentVersion(request.getIsCurrentVersion());
        entity.setOcrStatus(request.getOcrStatus());
        entity.setOcrText(request.getOcrText());
        return entity;
    }
}
