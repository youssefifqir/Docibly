package com.docibly.dms.ws.dto.organization.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Response DTO for Organization - User view.
 * Visible to: USER
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUserDto {

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
}
