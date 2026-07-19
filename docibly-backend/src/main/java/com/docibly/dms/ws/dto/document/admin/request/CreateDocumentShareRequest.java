package com.docibly.dms.ws.dto.document.admin.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.SharePermission;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocumentShareRequest {

    @NotBlank(message = "shareToken is required")
    @Size(max = 500, message = "shareToken must not exceed 500 characters")
    private String shareToken;
    private SharePermission permission;
    @Size(max = 500, message = "sharedWithEmail must not exceed 500 characters")
    private String sharedWithEmail;
    private Boolean isPublicLink;
    private LocalDateTime expiresAt;
    private Boolean isRevoked;
    private LocalDateTime revokedAt;
    @Min(value = 0, message = "accessCount must be positive")
    private Integer accessCount;
    private LocalDateTime lastAccessedAt;
    private Boolean requiresPassword;
    @Size(max = 500, message = "passwordHash must not exceed 500 characters")
    private String passwordHash;
}
