package com.docibly.dms.ws.dto.organization.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.enums.InvitationStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrganizationInvitationRequest {

    @Size(max = 500, message = "inviteeEmail must not exceed 500 characters")
    private String inviteeEmail;
    private MemberRole intendedRole;
    private InvitationStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime acceptedAt;
}
