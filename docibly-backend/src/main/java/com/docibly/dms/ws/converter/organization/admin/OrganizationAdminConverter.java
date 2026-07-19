package com.docibly.dms.ws.converter.organization.admin;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.ws.dto.organization.admin.request.CreateOrganizationRequest;
import com.docibly.dms.ws.dto.organization.admin.request.UpdateOrganizationRequest;
import com.docibly.dms.ws.dto.organization.admin.response.OrganizationAdminDto;

/**
 * Converter for Organization entity - Admin view.
 * Access: ADMIN
 */
@Component
public class OrganizationAdminConverter {

    public OrganizationAdminDto toDto(Organization entity) {
        if (entity == null) return null;
        OrganizationAdminDto dto = new OrganizationAdminDto();
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
        dto.setBillingEmail(entity.getBillingEmail());
        dto.setPlanTier(entity.getPlanTier());
        dto.setTrialEndsAt(entity.getTrialEndsAt());
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
        entity.setBillingEmail(request.getBillingEmail());
        entity.setPlanTier(request.getPlanTier());
        entity.setTrialEndsAt(request.getTrialEndsAt());
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
        entity.setBillingEmail(request.getBillingEmail());
        entity.setPlanTier(request.getPlanTier());
        entity.setTrialEndsAt(request.getTrialEndsAt());
        return entity;
    }
}
