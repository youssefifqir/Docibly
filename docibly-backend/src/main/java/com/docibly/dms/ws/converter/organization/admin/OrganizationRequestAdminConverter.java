package com.docibly.dms.ws.converter.organization.admin;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.organization.OrganizationRequest;
import com.docibly.dms.ws.dto.organization.admin.request.CreateOrganizationRequestRequest;
import com.docibly.dms.ws.dto.organization.admin.request.UpdateOrganizationRequestRequest;
import com.docibly.dms.ws.dto.organization.admin.response.OrganizationRequestAdminDto;

/**
 * Converter for OrganizationRequest entity - Admin view.
 * Access: ADMIN
 */
@Component
public class OrganizationRequestAdminConverter {

    public OrganizationRequestAdminDto toDto(OrganizationRequest entity) {
        if (entity == null) return null;
        OrganizationRequestAdminDto dto = new OrganizationRequestAdminDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setRequestedName(entity.getRequestedName());
        dto.setRequestedSlug(entity.getRequestedSlug());
        dto.setDescription(entity.getDescription());
        dto.setIntendedUse(entity.getIntendedUse());
        dto.setStatus(entity.getStatus());
        dto.setReviewedAt(entity.getReviewedAt());
        dto.setRejectionReason(entity.getRejectionReason());
        dto.setCreatedOrganizationId(entity.getCreatedOrganizationId());
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
        entity.setReviewedAt(request.getReviewedAt());
        entity.setRejectionReason(request.getRejectionReason());
        entity.setCreatedOrganizationId(request.getCreatedOrganizationId());
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
        entity.setReviewedAt(request.getReviewedAt());
        entity.setRejectionReason(request.getRejectionReason());
        entity.setCreatedOrganizationId(request.getCreatedOrganizationId());
        return entity;
    }
}
