package com.docibly.dms.ws.converter.organization.user;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.ws.dto.organization.user.request.CreateOrganizationInvitationRequest;
import com.docibly.dms.ws.dto.organization.user.request.UpdateOrganizationInvitationRequest;
import com.docibly.dms.ws.dto.organization.user.response.OrganizationInvitationUserDto;

/**
 * Converter for OrganizationInvitation entity - User view.
 * Access: USER
 */
@Component
public class OrganizationInvitationUserConverter {

    public OrganizationInvitationUserDto toDto(OrganizationInvitation entity) {
        if (entity == null) return null;
        OrganizationInvitationUserDto dto = new OrganizationInvitationUserDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setInviteeEmail(entity.getInviteeEmail());
        dto.setIntendedRole(entity.getIntendedRole());
        dto.setStatus(entity.getStatus());
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
        entity.setExpiresAt(request.getExpiresAt());
        entity.setAcceptedAt(request.getAcceptedAt());
        return entity;
    }
}
