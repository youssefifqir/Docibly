package com.docibly.dms.ws.dto.organization.admin.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Response DTO for Organization - Admin view.
 * Visible to: ADMIN
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationAdminDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private String website;
    private Long storageUsedBytes;
    private Long storageQuotaBytes;
    private Integer maxMembers;
    private Boolean isActive;
    private String billingEmail;
    private String planTier;
    private LocalDateTime trialEndsAt;
}
