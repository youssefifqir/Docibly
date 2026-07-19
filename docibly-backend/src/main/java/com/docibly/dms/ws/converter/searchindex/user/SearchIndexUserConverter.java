package com.docibly.dms.ws.converter.searchindex.user;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.searchindex.SearchIndex;
import com.docibly.dms.ws.dto.searchindex.user.request.CreateSearchIndexRequest;
import com.docibly.dms.ws.dto.searchindex.user.request.UpdateSearchIndexRequest;
import com.docibly.dms.ws.dto.searchindex.user.response.SearchIndexUserDto;

/**
 * Converter for SearchIndex entity - User view.
 * Access: USER
 */
@Component
public class SearchIndexUserConverter {

    public SearchIndexUserDto toDto(SearchIndex entity) {
        if (entity == null) return null;
        SearchIndexUserDto dto = new SearchIndexUserDto();
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
        dto.setVisibility(entity.getVisibility());
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
        entity.setVisibility(request.getVisibility());
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
        entity.setVisibility(request.getVisibility());
        return entity;
    }
}
