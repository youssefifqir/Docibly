package com.docibly.dms.ws.dto.organization.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.enums.InvitationStatus;

/**
 * Response DTO for OrganizationInvitation - User view.
 * Visible to: USER
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationInvitationUserDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String inviteeEmail;
    private MemberRole intendedRole;
    private InvitationStatus status;
    private LocalDateTime expiresAt;
    private LocalDateTime acceptedAt;
}
