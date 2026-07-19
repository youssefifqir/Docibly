
package com.docibly.dms.ws.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileUpdateRequest {
    
    @Schema(example = "John")
    private String firstName;

    @Schema(example = "Smith")
    private String lastName;

    @Schema(example = "avatarUrl")
    private String avatarUrl;

    @Schema(example = "bio")
    private String bio;

    @Schema(example = "jobTitle")
    private String jobTitle;

    private Long storageUsedBytes;

    private Long storageQuotaBytes;

    private LocalDateTime lastSeenAt;
}
