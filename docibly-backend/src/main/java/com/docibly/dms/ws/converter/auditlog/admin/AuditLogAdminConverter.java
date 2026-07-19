package com.docibly.dms.ws.converter.auditlog.admin;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.auditlog.AuditLog;
import com.docibly.dms.ws.dto.auditlog.admin.request.CreateAuditLogRequest;
import com.docibly.dms.ws.dto.auditlog.admin.request.UpdateAuditLogRequest;
import com.docibly.dms.ws.dto.auditlog.admin.response.AuditLogAdminDto;

/**
 * Converter for AuditLog entity - Admin view.
 * Access: ADMIN
 */
@Component
public class AuditLogAdminConverter {

    public AuditLogAdminDto toDto(AuditLog entity) {
        if (entity == null) return null;
        AuditLogAdminDto dto = new AuditLogAdminDto();
        dto.setId(entity.getId());
        dto.setRef(entity.getRef());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setAction(entity.getAction());
        dto.setActorUserId(entity.getActorUserId());
        dto.setActorEmail(entity.getActorEmail());
        dto.setTargetEntityType(entity.getTargetEntityType());
        dto.setTargetEntityId(entity.getTargetEntityId());
        dto.setOrganizationId(entity.getOrganizationId());
        dto.setMetadata(entity.getMetadata());
        dto.setIpAddress(entity.getIpAddress());
        dto.setUserAgent(entity.getUserAgent());
        return dto;
    }

    public AuditLog toEntity(CreateAuditLogRequest request) {
        if (request == null) return null;
        AuditLog entity = new AuditLog();
        entity.setAction(request.getAction());
        entity.setActorUserId(request.getActorUserId());
        entity.setActorEmail(request.getActorEmail());
        entity.setTargetEntityType(request.getTargetEntityType());
        entity.setTargetEntityId(request.getTargetEntityId());
        entity.setOrganizationId(request.getOrganizationId());
        entity.setMetadata(request.getMetadata());
        entity.setIpAddress(request.getIpAddress());
        entity.setUserAgent(request.getUserAgent());
        return entity;
    }

    public AuditLog toEntity(UpdateAuditLogRequest request) {
        if (request == null) return null;
        AuditLog entity = new AuditLog();
        entity.setAction(request.getAction());
        entity.setActorUserId(request.getActorUserId());
        entity.setActorEmail(request.getActorEmail());
        entity.setTargetEntityType(request.getTargetEntityType());
        entity.setTargetEntityId(request.getTargetEntityId());
        entity.setOrganizationId(request.getOrganizationId());
        entity.setMetadata(request.getMetadata());
        entity.setIpAddress(request.getIpAddress());
        entity.setUserAgent(request.getUserAgent());
        return entity;
    }
}
