package com.docibly.dms.ws.converter.document.admin;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.ws.dto.document.admin.request.CreateDocumentRequest;
import com.docibly.dms.ws.dto.document.admin.request.UpdateDocumentRequest;
import com.docibly.dms.ws.dto.document.admin.response.DocumentAdminDto;

/**
 * Converter for Document entity - Admin view.
 * Access: ADMIN
 */
@Component
public class DocumentAdminConverter {

    public DocumentAdminDto toDto(Document entity) {
        if (entity == null) return null;
        DocumentAdminDto dto = new DocumentAdminDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setOriginalFilename(entity.getOriginalFilename());
        dto.setStoredFilename(entity.getStoredFilename());
        dto.setMimeType(entity.getMimeType());
        dto.setFileSizeBytes(entity.getFileSizeBytes());
        dto.setStorageBucket(entity.getStorageBucket());
        dto.setStorageKey(entity.getStorageKey());
        dto.setStatus(entity.getStatus());
        dto.setVisibility(entity.getVisibility());
        dto.setCurrentVersionNumber(entity.getCurrentVersionNumber());
        dto.setDownloadCount(entity.getDownloadCount());
        dto.setViewCount(entity.getViewCount());
        dto.setIsPasswordProtected(entity.getIsPasswordProtected());
        dto.setPasswordHash(entity.getPasswordHash());
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setArchivedAt(entity.getArchivedAt());
        dto.setChecksum(entity.getChecksum());
        dto.setOcrStatus(entity.getOcrStatus());
        dto.setOcrText(entity.getOcrText());
        dto.setOcrProcessedAt(entity.getOcrProcessedAt());
        dto.setOcrLanguage(entity.getOcrLanguage());
        dto.setOcrConfidenceScore(entity.getOcrConfidenceScore());
        return dto;
    }

    public Document toEntity(CreateDocumentRequest request) {
        if (request == null) return null;
        Document entity = new Document();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setOriginalFilename(request.getOriginalFilename());
        entity.setStoredFilename(request.getStoredFilename());
        entity.setMimeType(request.getMimeType());
        entity.setFileSizeBytes(request.getFileSizeBytes());
        entity.setStorageBucket(request.getStorageBucket());
        entity.setStorageKey(request.getStorageKey());
        entity.setStatus(request.getStatus());
        entity.setVisibility(request.getVisibility());
        entity.setCurrentVersionNumber(request.getCurrentVersionNumber());
        entity.setDownloadCount(request.getDownloadCount());
        entity.setViewCount(request.getViewCount());
        entity.setIsPasswordProtected(request.getIsPasswordProtected());
        entity.setPasswordHash(request.getPasswordHash());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setArchivedAt(request.getArchivedAt());
        entity.setChecksum(request.getChecksum());
        entity.setOcrStatus(request.getOcrStatus());
        entity.setOcrText(request.getOcrText());
        entity.setOcrProcessedAt(request.getOcrProcessedAt());
        entity.setOcrLanguage(request.getOcrLanguage());
        entity.setOcrConfidenceScore(request.getOcrConfidenceScore());
        return entity;
    }

    public Document toEntity(UpdateDocumentRequest request) {
        if (request == null) return null;
        Document entity = new Document();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setOriginalFilename(request.getOriginalFilename());
        entity.setStoredFilename(request.getStoredFilename());
        entity.setMimeType(request.getMimeType());
        entity.setFileSizeBytes(request.getFileSizeBytes());
        entity.setStorageBucket(request.getStorageBucket());
        entity.setStorageKey(request.getStorageKey());
        entity.setStatus(request.getStatus());
        entity.setVisibility(request.getVisibility());
        entity.setCurrentVersionNumber(request.getCurrentVersionNumber());
        entity.setDownloadCount(request.getDownloadCount());
        entity.setViewCount(request.getViewCount());
        entity.setIsPasswordProtected(request.getIsPasswordProtected());
        entity.setPasswordHash(request.getPasswordHash());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setArchivedAt(request.getArchivedAt());
        entity.setChecksum(request.getChecksum());
        entity.setOcrStatus(request.getOcrStatus());
        entity.setOcrText(request.getOcrText());
        entity.setOcrProcessedAt(request.getOcrProcessedAt());
        entity.setOcrLanguage(request.getOcrLanguage());
        entity.setOcrConfidenceScore(request.getOcrConfidenceScore());
        return entity;
    }
}
