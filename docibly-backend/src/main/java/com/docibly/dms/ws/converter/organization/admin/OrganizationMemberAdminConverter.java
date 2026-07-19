package com.docibly.dms.ws.converter.organization.admin;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.ws.dto.organization.admin.request.CreateOrganizationMemberRequest;
import com.docibly.dms.ws.dto.organization.admin.request.UpdateOrganizationMemberRequest;
import com.docibly.dms.ws.dto.organization.admin.response.OrganizationMemberAdminDto;

/**
 * Converter for OrganizationMember entity - Admin view.
 * Access: ADMIN
 */
@Component
public class OrganizationMemberAdminConverter {

    public OrganizationMemberAdminDto toDto(OrganizationMember entity) {
        if (entity == null) return null;
        OrganizationMemberAdminDto dto = new OrganizationMemberAdminDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setMemberRole(entity.getMemberRole());
        dto.setJoinedAt(entity.getJoinedAt());
        dto.setIsActive(entity.getIsActive());
        dto.setInvitedByEmail(entity.getInvitedByEmail());
        return dto;
    }

    public OrganizationMember toEntity(CreateOrganizationMemberRequest request) {
        if (request == null) return null;
        OrganizationMember entity = new OrganizationMember();
        entity.setMemberRole(request.getMemberRole());
        entity.setJoinedAt(request.getJoinedAt());
        entity.setIsActive(request.getIsActive());
        entity.setInvitedByEmail(request.getInvitedByEmail());
        return entity;
    }

    public OrganizationMember toEntity(UpdateOrganizationMemberRequest request) {
        if (request == null) return null;
        OrganizationMember entity = new OrganizationMember();
        entity.setMemberRole(request.getMemberRole());
        entity.setJoinedAt(request.getJoinedAt());
        entity.setIsActive(request.getIsActive());
        entity.setInvitedByEmail(request.getInvitedByEmail());
        return entity;
    }
}
