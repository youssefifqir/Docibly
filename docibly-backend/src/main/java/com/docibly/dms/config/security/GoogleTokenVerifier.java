package com.docibly.dms.config.security;

import com.docibly.dms.bean.core.user.AuthProvider;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class GoogleTokenVerifier {

    private final GoogleIdTokenVerifier idTokenVerifier;

    @Value("${app.security.oauth.google.enabled:false}")
    private boolean enabled;

    public GoogleTokenVerifier(
            @Value("${app.security.oauth.google.client-id:}") final String clientId) {
        this.idTokenVerifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleUserInfo verify(final String idTokenString) {
        if (!this.enabled) {
            throw new BusinessException(ErrorCode.GOOGLE_AUTH_DISABLED);
        }
        try {
            final GoogleIdToken idToken = this.idTokenVerifier.verify(idTokenString);
            if (idToken == null) {
                throw new BusinessException(ErrorCode.GOOGLE_TOKEN_INVALID);
            }
            final GoogleIdToken.Payload payload = idToken.getPayload();
            final String email = payload.getEmail();
            if (email == null || email.isBlank()) {
                throw new BusinessException(ErrorCode.GOOGLE_AUTH_EMAIL_MISSING);
            }
            return new GoogleUserInfo(
                    payload.getSubject(),
                    email,
                    Boolean.TRUE.equals(payload.getEmailVerified()),
                    (String) payload.getOrDefault("given_name", ""),
                    (String) payload.getOrDefault("family_name", "")
            );
        } catch (final BusinessException e) {
            throw e;
        } catch (final Exception e) {
            log.warn("Google ID token verification failed: {}", e.getMessage());
            throw new BusinessException(ErrorCode.GOOGLE_TOKEN_INVALID);
        }
    }

    public record GoogleUserInfo(
            String sub,
            String email,
            boolean emailVerified,
            String firstName,
            String lastName
    ) {}
}
