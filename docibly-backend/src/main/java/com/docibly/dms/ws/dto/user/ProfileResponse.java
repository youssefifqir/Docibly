package com.docibly.dms.ws.dto.user;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String avatarUrl;
    private String bio;
    private String jobTitle;
    private boolean enabled;
    private boolean emailVerified;
    private LocalDateTime createdDate;
    private String planTier;
    private Long storageUsedBytes;
    private Long storageQuotaBytes;
    private String authProvider;
    private List<String> roles;
}
