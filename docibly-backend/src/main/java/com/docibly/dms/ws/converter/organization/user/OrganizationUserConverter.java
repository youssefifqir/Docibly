package com.docibly.dms.ws.converter.organization.user;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.ws.dto.organization.user.request.CreateOrganizationRequest;
import com.docibly.dms.ws.dto.organization.user.request.UpdateOrganizationRequest;
import com.docibly.dms.ws.dto.organization.user.response.OrganizationUserDto;

/**
 * Converter for Organization entity - User view.
 * Access: USER
 */
@Component
public class OrganizationUserConverter {

    public OrganizationUserDto toDto(Organization entity) {
        if (entity == null) return null;
        OrganizationUserDto dto = new OrganizationUserDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setName(entity.getName());
        dto.setSlug(entity.getSlug());
        dto.setDescription(entity.getDescription());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setWebsite(entity.getWebsite());
        dto.setStorageUsedBytes(entity.getStorageUsedBytes());
        dto.setStorageQuotaBytes(entity.getStorageQuotaBytes());
        dto.setMaxMembers(entity.getMaxMembers());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public Organization toEntity(CreateOrganizationRequest request) {
        if (request == null) return null;
        Organization entity = new Organization();
        entity.setName(request.getName());
        entity.setSlug(request.getSlug());
        entity.setDescription(request.getDescription());
        entity.setLogoUrl(request.getLogoUrl());
        entity.setWebsite(request.getWebsite());
        entity.setStorageUsedBytes(request.getStorageUsedBytes());
        entity.setStorageQuotaBytes(request.getStorageQuotaBytes());
        entity.setMaxMembers(request.getMaxMembers());
        entity.setIsActive(request.getIsActive());
        return entity;
    }

    public Organization toEntity(UpdateOrganizationRequest request) {
        if (request == null) return null;
        Organization entity = new Organization();
        entity.setName(request.getName());
        entity.setSlug(request.getSlug());
        entity.setDescription(request.getDescription());
        entity.setLogoUrl(request.getLogoUrl());
        entity.setWebsite(request.getWebsite());
        entity.setStorageUsedBytes(request.getStorageUsedBytes());
        entity.setStorageQuotaBytes(request.getStorageQuotaBytes());
        entity.setMaxMembers(request.getMaxMembers());
        entity.setIsActive(request.getIsActive());
        return entity;
    }
}
