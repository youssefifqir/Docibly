
package com.docibly.dms.ws.converter.user;

import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.bean.core.user.AuthProvider;
import com.docibly.dms.ws.dto.auth.RegistrationRequest;
import com.docibly.dms.ws.dto.user.ProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {
    
    private final PasswordEncoder passwordEncoder;

    public User toUser(final RegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(this.passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .locked(false)
                .credentialsExpired(false)
                .emailVerified(false)
                .phoneVerified(false)
                .authProvider(AuthProvider.LOCAL)
                .build();
    }

    public void mergeUserInfo(final User user, final ProfileUpdateRequest request) {
        if (StringUtils.isNotBlank(request.getFirstName()) && !user.getFirstName()
                .equals(request.getFirstName())) {
            user.setFirstName(request.getFirstName());
        }
        if (StringUtils.isNotBlank(request.getLastName()) && !user.getLastName()
                .equals(request.getLastName())) {
            user.setLastName(request.getLastName());
        }
        if (StringUtils.isNotBlank(request.getAvatarUrl()) && !request.getAvatarUrl()
                .equals(user.getAvatarUrl())) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (StringUtils.isNotBlank(request.getBio()) && !request.getBio()
                .equals(user.getBio())) {
            user.setBio(request.getBio());
        }
        if (StringUtils.isNotBlank(request.getJobTitle()) && !request.getJobTitle()
                .equals(user.getJobTitle())) {
            user.setJobTitle(request.getJobTitle());
        }
        if (request.getStorageUsedBytes() != null && !request.getStorageUsedBytes()
                .equals(user.getStorageUsedBytes())) {
            user.setStorageUsedBytes(request.getStorageUsedBytes());
        }
        if (request.getStorageQuotaBytes() != null && !request.getStorageQuotaBytes()
                .equals(user.getStorageQuotaBytes())) {
            user.setStorageQuotaBytes(request.getStorageQuotaBytes());
        }
        if (request.getLastSeenAt() != null && !request.getLastSeenAt()
                .equals(user.getLastSeenAt())) {
            user.setLastSeenAt(request.getLastSeenAt());
        }
    }
}
