package com.docibly.dms.ws.converter.organization.admin;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.ws.dto.organization.admin.request.CreateOrganizationInvitationRequest;
import com.docibly.dms.ws.dto.organization.admin.request.UpdateOrganizationInvitationRequest;
import com.docibly.dms.ws.dto.organization.admin.response.OrganizationInvitationAdminDto;

/**
 * Converter for OrganizationInvitation entity - Admin view.
 * Access: ADMIN
 */
@Component
public class OrganizationInvitationAdminConverter {

    public OrganizationInvitationAdminDto toDto(OrganizationInvitation entity) {
        if (entity == null) return null;
        OrganizationInvitationAdminDto dto = new OrganizationInvitationAdminDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setInviteeEmail(entity.getInviteeEmail());
        dto.setIntendedRole(entity.getIntendedRole());
        dto.setStatus(entity.getStatus());
        dto.setToken(entity.getToken());
        dto.setExpiresAt(entity.getExpiresAt());
        dto.setAcceptedAt(entity.getAcceptedAt());
        return dto;
    }

    public OrganizationInvitation toEntity(CreateOrganizationInvitationRequest request) {
        if (request == null) return null;
        OrganizationInvitation entity = new OrganizationInvitation();
        entity.setInviteeEmail(request.getInviteeEmail());
        entity.setIntendedRole(request.getIntendedRole());
        entity.setStatus(request.getStatus());
        entity.setToken(request.getToken());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setAcceptedAt(request.getAcceptedAt());
        return entity;
    }

    public OrganizationInvitation toEntity(UpdateOrganizationInvitationRequest request) {
        if (request == null) return null;
        OrganizationInvitation entity = new OrganizationInvitation();
        entity.setInviteeEmail(request.getInviteeEmail());
        entity.setIntendedRole(request.getIntendedRole());
        entity.setStatus(request.getStatus());
        entity.setToken(request.getToken());
        entity.setExpiresAt(request.getExpiresAt());
        entity.setAcceptedAt(request.getAcceptedAt());
        return entity;
    }
}
