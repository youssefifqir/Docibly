package com.docibly.dms.ws.converter.organization.user;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.ws.dto.organization.user.request.CreateOrganizationMemberRequest;
import com.docibly.dms.ws.dto.organization.user.request.UpdateOrganizationMemberRequest;
import com.docibly.dms.ws.dto.organization.user.response.OrganizationMemberUserDto;

/**
 * Converter for OrganizationMember entity - User view.
 * Access: USER
 */
@Component
public class OrganizationMemberUserConverter {

    public OrganizationMemberUserDto toDto(OrganizationMember entity) {
        if (entity == null) return null;
        OrganizationMemberUserDto dto = new OrganizationMemberUserDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setMemberRole(entity.getMemberRole());
        dto.setJoinedAt(entity.getJoinedAt());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public OrganizationMember toEntity(CreateOrganizationMemberRequest request) {
        if (request == null) return null;
        OrganizationMember entity = new OrganizationMember();
        entity.setMemberRole(request.getMemberRole());
        entity.setJoinedAt(request.getJoinedAt());
        entity.setIsActive(request.getIsActive());
        return entity;
    }

    public OrganizationMember toEntity(UpdateOrganizationMemberRequest request) {
        if (request == null) return null;
        OrganizationMember entity = new OrganizationMember();
        entity.setMemberRole(request.getMemberRole());
        entity.setJoinedAt(request.getJoinedAt());
        entity.setIsActive(request.getIsActive());
        return entity;
    }
}
