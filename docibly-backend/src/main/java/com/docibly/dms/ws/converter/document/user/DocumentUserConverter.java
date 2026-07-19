package com.docibly.dms.ws.converter.document.user;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.ws.dto.document.user.request.CreateDocumentRequest;
import com.docibly.dms.ws.dto.document.user.request.UpdateDocumentRequest;
import com.docibly.dms.ws.dto.document.user.response.DocumentUserDto;

/**
 * Converter for Document entity - User view.
 * Access: USER
 */
@Component
public class DocumentUserConverter {

    public DocumentUserDto toDto(Document entity) {
        if (entity == null) return null;
        DocumentUserDto dto = new DocumentUserDto();
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
        dto.setStatus(entity.getStatus());
        dto.setVisibility(entity.getVisibility());
        dto.setCurrentVersionNumber(entity.getCurrentVersionNumber());
        dto.setDownloadCount(entity.getDownloadCount());
        dto.setViewCount(entity.getViewCount());
        dto.setIsPasswordProtected(entity.getIsPasswordProtected());
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setArchivedAt(entity.getArchivedAt());
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
        entity.setStatus(request.getStatus());
        entity.setVisibility(request.getVisibility());
        entity.setCurrentVersionNumber(request.getCurrentVersionNumber());
        entity.setDownloadCount(request.getDownloadCount());
        entity.setViewCount(request.getViewCount());
        entity.setIsPasswordProtected(request.getIsPasswordProtected());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setArchivedAt(request.getArchivedAt());
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
        entity.setStatus(request.getStatus());
        entity.setVisibility(request.getVisibility());
        entity.setCurrentVersionNumber(request.getCurrentVersionNumber());
        entity.setDownloadCount(request.getDownloadCount());
        entity.setViewCount(request.getViewCount());
        entity.setIsPasswordProtected(request.getIsPasswordProtected());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setArchivedAt(request.getArchivedAt());
        entity.setOcrStatus(request.getOcrStatus());
        entity.setOcrText(request.getOcrText());
        entity.setOcrProcessedAt(request.getOcrProcessedAt());
        entity.setOcrLanguage(request.getOcrLanguage());
        entity.setOcrConfidenceScore(request.getOcrConfidenceScore());
        return entity;
    }
}
