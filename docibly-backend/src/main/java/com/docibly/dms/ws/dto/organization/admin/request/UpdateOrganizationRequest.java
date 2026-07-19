package com.docibly.dms.ws.dto.organization.admin.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrganizationRequest {

    @Size(max = 500, message = "name must not exceed 500 characters")
    private String name;
    @Size(max = 500, message = "slug must not exceed 500 characters")
    private String slug;
    @Size(max = 500, message = "description must not exceed 500 characters")
    private String description;
    @Size(max = 500, message = "logoUrl must not exceed 500 characters")
    private String logoUrl;
    @Size(max = 500, message = "website must not exceed 500 characters")
    private String website;
    @Min(value = 0, message = "storageUsedBytes must be positive")
    private Long storageUsedBytes;
    @Min(value = 0, message = "storageQuotaBytes must be positive")
    private Long storageQuotaBytes;
    @Min(value = 0, message = "maxMembers must be positive")
    private Integer maxMembers;
    private Boolean isActive;
    @Size(max = 500, message = "billingEmail must not exceed 500 characters")
    private String billingEmail;
    @Size(max = 500, message = "planTier must not exceed 500 characters")
    private String planTier;
    private LocalDateTime trialEndsAt;
}
