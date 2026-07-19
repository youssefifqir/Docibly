package com.docibly.dms.ws.dto.auditlog.admin.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import com.docibly.dms.bean.core.enums.AuditAction;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAuditLogRequest {

    private AuditAction action;
    @Size(max = 500, message = "actorUserId must not exceed 500 characters")
    private String actorUserId;
    @Size(max = 500, message = "actorEmail must not exceed 500 characters")
    private String actorEmail;
    @Size(max = 500, message = "targetEntityType must not exceed 500 characters")
    private String targetEntityType;
    @Size(max = 500, message = "targetEntityId must not exceed 500 characters")
    private String targetEntityId;
    @Min(value = 0, message = "organizationId must be positive")
    private Long organizationId;
    @Size(max = 500, message = "metadata must not exceed 500 characters")
    private String metadata;
    @Size(max = 500, message = "ipAddress must not exceed 500 characters")
    private String ipAddress;
    @Size(max = 500, message = "userAgent must not exceed 500 characters")
    private String userAgent;
}
