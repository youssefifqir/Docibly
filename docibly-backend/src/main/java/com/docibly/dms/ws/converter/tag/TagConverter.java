package com.docibly.dms.ws.converter.tag;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.tag.Tag;
import com.docibly.dms.ws.dto.tag.request.CreateTagRequest;
import com.docibly.dms.ws.dto.tag.request.UpdateTagRequest;
import com.docibly.dms.ws.dto.tag.response.TagResponse;

@Component
public class TagConverter {

    public TagResponse toResponse(Tag entity) {
        if (entity == null) return null;
        TagResponse response = new TagResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setName(entity.getName());
        response.setSlug(entity.getSlug());
        response.setColor(entity.getColor());
        response.setUsageCount(entity.getUsageCount());
        if (entity.getOrganization() != null) {
            response.setOrganizationId(entity.getOrganization().getId());
            response.setOrganizationRef(entity.getOrganization().getRef());
        }
        return response;
    }

    public Tag toEntity(CreateTagRequest request) {
        if (request == null) return null;
        Tag entity = new Tag();
        entity.setName(request.getName());
        entity.setSlug(request.getSlug());
        entity.setColor(request.getColor());
        entity.setUsageCount(request.getUsageCount());
        return entity;
    }

    public Tag toEntity(UpdateTagRequest request) {
        if (request == null) return null;
        Tag entity = new Tag();
        entity.setName(request.getName());
        entity.setSlug(request.getSlug());
        entity.setColor(request.getColor());
        entity.setUsageCount(request.getUsageCount());
        return entity;
    }
}

