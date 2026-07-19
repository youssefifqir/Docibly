package com.docibly.dms.ws.dto.organization.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrganizationRequest {

    @NotBlank(message = "name is required")
    @Size(max = 500, message = "name must not exceed 500 characters")
    private String name;
    @NotBlank(message = "slug is required")
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
}
