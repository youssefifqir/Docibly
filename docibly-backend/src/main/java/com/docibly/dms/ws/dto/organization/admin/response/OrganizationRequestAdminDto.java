package com.docibly.dms.ws.dto.organization.admin.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.OrgRequestStatus;

/**
 * Response DTO for OrganizationRequest - Admin view.
 * Visible to: ADMIN
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRequestAdminDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String requestedName;
    private String requestedSlug;
    private String description;
    private String intendedUse;
    private OrgRequestStatus status;
    private LocalDateTime reviewedAt;
    private String rejectionReason;
    private Long createdOrganizationId;
}
