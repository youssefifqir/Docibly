package com.docibly.dms.ws.converter.auditlog.user;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.auditlog.AuditLog;
import com.docibly.dms.ws.dto.auditlog.user.request.CreateAuditLogRequest;
import com.docibly.dms.ws.dto.auditlog.user.request.UpdateAuditLogRequest;
import com.docibly.dms.ws.dto.auditlog.user.response.AuditLogUserDto;

/**
 * Converter for AuditLog entity - User view.
 * Access: USER
 */
@Component
public class AuditLogUserConverter {

    public AuditLogUserDto toDto(AuditLog entity) {
        if (entity == null) return null;
        AuditLogUserDto dto = new AuditLogUserDto();
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
        return entity;
    }
}
