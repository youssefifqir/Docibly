package com.docibly.dms.ws.dto.document.user.request;

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
public class UpdateDocumentShareRequest {

    private SharePermission permission;
    @Size(max = 500, message = "sharedWithEmail must not exceed 500 characters")
    private String sharedWithEmail;
    private Boolean isPublicLink;
    private LocalDateTime expiresAt;
    private Boolean isRevoked;
    @Min(value = 0, message = "accessCount must be positive")
    private Integer accessCount;
    private LocalDateTime lastAccessedAt;
    private Boolean requiresPassword;
}
