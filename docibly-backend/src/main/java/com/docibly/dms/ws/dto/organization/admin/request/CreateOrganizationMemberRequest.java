package com.docibly.dms.ws.dto.organization.admin.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.MemberRole;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationMemberRequest {

    private MemberRole memberRole;
    private LocalDateTime joinedAt;
    private Boolean isActive;
    @Size(max = 500, message = "invitedByEmail must not exceed 500 characters")
    private String invitedByEmail;
}
