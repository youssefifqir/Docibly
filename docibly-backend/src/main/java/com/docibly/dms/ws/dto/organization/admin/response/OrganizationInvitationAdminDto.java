package com.docibly.dms.ws.dto.organization.admin.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.enums.InvitationStatus;

/**
 * Response DTO for OrganizationInvitation - Admin view.
 * Visible to: ADMIN
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationInvitationAdminDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String inviteeEmail;
    private MemberRole intendedRole;
    private InvitationStatus status;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime acceptedAt;
}
