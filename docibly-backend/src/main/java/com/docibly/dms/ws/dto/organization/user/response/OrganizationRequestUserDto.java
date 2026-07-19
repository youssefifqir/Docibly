package com.docibly.dms.ws.dto.organization.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.OrgRequestStatus;

/**
 * Response DTO for OrganizationRequest - User view.
 * Visible to: USER
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationRequestUserDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String requestedName;
    private String requestedSlug;
    private String description;
    private String intendedUse;
    private OrgRequestStatus status;
    private String rejectionReason;
}
