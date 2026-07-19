package com.docibly.dms.ws.dto.document.admin.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.SharePermission;

/**
 * Response DTO for DocumentShare - Admin view.
 * Visible to: ADMIN
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentShareAdminDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String shareToken;
    private SharePermission permission;
    private String sharedWithEmail;
    private Boolean isPublicLink;
    private LocalDateTime expiresAt;
    private Boolean isRevoked;
    private LocalDateTime revokedAt;
    private Integer accessCount;
    private LocalDateTime lastAccessedAt;
    private Boolean requiresPassword;
    private String passwordHash;
}
