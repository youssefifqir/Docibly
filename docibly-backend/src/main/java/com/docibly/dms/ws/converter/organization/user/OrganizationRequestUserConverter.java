package com.docibly.dms.ws.converter.organization.user;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.organization.OrganizationRequest;
import com.docibly.dms.ws.dto.organization.user.request.CreateOrganizationRequestRequest;
import com.docibly.dms.ws.dto.organization.user.request.UpdateOrganizationRequestRequest;
import com.docibly.dms.ws.dto.organization.user.response.OrganizationRequestUserDto;

/**
 * Converter for OrganizationRequest entity - User view.
 * Access: USER
 */
@Component
public class OrganizationRequestUserConverter {

    public OrganizationRequestUserDto toDto(OrganizationRequest entity) {
        if (entity == null) return null;
        OrganizationRequestUserDto dto = new OrganizationRequestUserDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setRequestedName(entity.getRequestedName());
        dto.setRequestedSlug(entity.getRequestedSlug());
        dto.setDescription(entity.getDescription());
        dto.setIntendedUse(entity.getIntendedUse());
        dto.setStatus(entity.getStatus());
        dto.setRejectionReason(entity.getRejectionReason());
        return dto;
    }

    public OrganizationRequest toEntity(CreateOrganizationRequestRequest request) {
        if (request == null) return null;
        OrganizationRequest entity = new OrganizationRequest();
        entity.setRequestedName(request.getRequestedName());
        entity.setRequestedSlug(request.getRequestedSlug());
        entity.setDescription(request.getDescription());
        entity.setIntendedUse(request.getIntendedUse());
        entity.setStatus(request.getStatus());
        entity.setRejectionReason(request.getRejectionReason());
        return entity;
    }

    public OrganizationRequest toEntity(UpdateOrganizationRequestRequest request) {
        if (request == null) return null;
        OrganizationRequest entity = new OrganizationRequest();
        entity.setRequestedName(request.getRequestedName());
        entity.setRequestedSlug(request.getRequestedSlug());
        entity.setDescription(request.getDescription());
        entity.setIntendedUse(request.getIntendedUse());
        entity.setStatus(request.getStatus());
        entity.setRejectionReason(request.getRejectionReason());
        return entity;
    }
}
