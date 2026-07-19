package com.docibly.dms.ws.converter.searchindex.admin;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.searchindex.SearchIndex;
import com.docibly.dms.ws.dto.searchindex.admin.request.CreateSearchIndexRequest;
import com.docibly.dms.ws.dto.searchindex.admin.request.UpdateSearchIndexRequest;
import com.docibly.dms.ws.dto.searchindex.admin.response.SearchIndexAdminDto;

/**
 * Converter for SearchIndex entity - Admin view.
 * Access: ADMIN
 */
@Component
public class SearchIndexAdminConverter {

    public SearchIndexAdminDto toDto(SearchIndex entity) {
        if (entity == null) return null;
        SearchIndexAdminDto dto = new SearchIndexAdminDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setDocumentId(entity.getDocumentId());
        dto.setDocumentTitle(entity.getDocumentTitle());
        dto.setFullText(entity.getFullText());
        dto.setOcrText(entity.getOcrText());
        dto.setTags(entity.getTags());
        dto.setMimeType(entity.getMimeType());
        dto.setOrganizationId(entity.getOrganizationId());
        dto.setOwnerId(entity.getOwnerId());
        dto.setVisibility(entity.getVisibility());
        dto.setIndexedAt(entity.getIndexedAt());
        return dto;
    }

    public SearchIndex toEntity(CreateSearchIndexRequest request) {
        if (request == null) return null;
        SearchIndex entity = new SearchIndex();
        entity.setDocumentId(request.getDocumentId());
        entity.setDocumentTitle(request.getDocumentTitle());
        entity.setFullText(request.getFullText());
        entity.setOcrText(request.getOcrText());
        entity.setTags(request.getTags());
        entity.setMimeType(request.getMimeType());
        entity.setOrganizationId(request.getOrganizationId());
        entity.setOwnerId(request.getOwnerId());
        entity.setVisibility(request.getVisibility());
        entity.setIndexedAt(request.getIndexedAt());
        return entity;
    }

    public SearchIndex toEntity(UpdateSearchIndexRequest request) {
        if (request == null) return null;
        SearchIndex entity = new SearchIndex();
        entity.setDocumentId(request.getDocumentId());
        entity.setDocumentTitle(request.getDocumentTitle());
        entity.setFullText(request.getFullText());
        entity.setOcrText(request.getOcrText());
        entity.setTags(request.getTags());
        entity.setMimeType(request.getMimeType());
        entity.setOrganizationId(request.getOrganizationId());
        entity.setOwnerId(request.getOwnerId());
        entity.setVisibility(request.getVisibility());
        entity.setIndexedAt(request.getIndexedAt());
        return entity;
    }
}
