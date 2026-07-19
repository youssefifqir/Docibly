package com.docibly.dms.ws.dto.organization.user.request;

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
public class UpdateOrganizationMemberRequest {

    private MemberRole memberRole;
    private LocalDateTime joinedAt;
    private Boolean isActive;
}
