package com.docibly.dms.ws.dto.document.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.SharePermission;

/**
 * Response DTO for DocumentShare - User view.
 * Visible to: USER
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentShareUserDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private SharePermission permission;
    private String sharedWithEmail;
    private Boolean isPublicLink;
    private LocalDateTime expiresAt;
    private Boolean isRevoked;
    private Integer accessCount;
    private LocalDateTime lastAccessedAt;
    private Boolean requiresPassword;
}
