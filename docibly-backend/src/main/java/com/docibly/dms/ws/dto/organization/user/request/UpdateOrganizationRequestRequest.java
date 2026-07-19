package com.docibly.dms.ws.dto.organization.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import com.docibly.dms.bean.core.enums.OrgRequestStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrganizationRequestRequest {

    @Size(max = 500, message = "requestedName must not exceed 500 characters")
    private String requestedName;
    @Size(max = 500, message = "requestedSlug must not exceed 500 characters")
    private String requestedSlug;
    @Size(max = 500, message = "description must not exceed 500 characters")
    private String description;
    @Size(max = 500, message = "intendedUse must not exceed 500 characters")
    private String intendedUse;
    private OrgRequestStatus status;
    @Size(max = 500, message = "rejectionReason must not exceed 500 characters")
    private String rejectionReason;
}
