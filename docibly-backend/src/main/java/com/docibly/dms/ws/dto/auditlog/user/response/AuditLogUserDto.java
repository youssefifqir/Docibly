package com.docibly.dms.ws.dto.auditlog.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.AuditAction;

/**
 * Response DTO for AuditLog - User view.
 * Visible to: USER
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogUserDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private AuditAction action;
    private String actorUserId;
    private String actorEmail;
    private String targetEntityType;
    private String targetEntityId;
    private Long organizationId;
    private String metadata;
}
