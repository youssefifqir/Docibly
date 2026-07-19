package com.docibly.dms.service.impl.security;

import com.docibly.dms.bean.core.role.Role;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.config.security.TokenDenylist;
import com.docibly.dms.service.facade.email.PasswordResetService;
import com.docibly.dms.bean.core.user.AuthProvider;
import com.docibly.dms.config.security.GoogleTokenVerifier;
import com.docibly.dms.config.security.GoogleTokenVerifier.GoogleUserInfo;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.dao.facade.security.RoleDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.service.facade.security.AuthenticationService;
import com.docibly.dms.service.facade.security.JwtService;
import com.docibly.dms.ws.converter.user.UserConverter;
import com.docibly.dms.ws.dto.auth.AuthenticationRequest;
import com.docibly.dms.ws.dto.auth.RefreshRequest;
import com.docibly.dms.ws.dto.auth.RegistrationRequest;
import com.docibly.dms.ws.dto.auth.AuthenticationResponse;
import com.docibly.dms.ws.dto.auth.GoogleAuthRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.docibly.dms.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final TokenDenylist tokenDenylist;
    private final PasswordResetService passwordResetService;
    private final GoogleTokenVerifier googleTokenVerifier;

    @Value("${app.security.oauth.google.auto-create-user:true}")
    private boolean autoCreateUser;

    @Value("${app.security.oauth.google.default-role:ROLE_USER}")
    private String googleDefaultRole;

    @Value("${app.security.jwt.remember-me-token-expiration:2592000000}")
    private long rememberMeTokenExpiration;

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponse login(final AuthenticationRequest request) {
        final Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        final User user = (User) auth.getPrincipal();
        if (!user.isEmailVerified()) {
            throw new BusinessException(TOKEN_INVALID);
        }
        final String token = this.jwtService.generateAccessToken(user.getUsername());
        final String refreshToken = request.isRememberMe()
                ? this.jwtService.generateRefreshToken(user.getUsername(), this.rememberMeTokenExpiration)
                : this.jwtService.generateRefreshToken(user.getUsername());
        final String tokenType = "Bearer";
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .tokenType(tokenType)
                .build();
    }

    @Override
    @Transactional(timeout = 30)
    public void register(final RegistrationRequest request) {
        checkPasswords(request.getPassword(), request.getConfirmPassword());

        Role defaultRole = this.roleDao.findByName("ROLE_USER")
                .orElseGet(() -> this.roleDao.findAll().stream()
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No roles configured in the system")));
        
        final List<Role> roles = new ArrayList<>();
        roles.add(defaultRole);

        final User user = this.userConverter.toUser(request);
        user.setRoles(roles);
        log.debug("Saving user {}", user);
        try {
            this.userDao.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            String constraint = extractConstraintName(e);
            if (constraint != null && constraint.toLowerCase().contains("email")) {
                throw new BusinessException(EMAIL_ALREADY_EXISTS);
            }
            if (constraint != null && constraint.toLowerCase().contains("phone")) {
                throw new BusinessException(PHONE_ALREADY_EXISTS);
            }
            throw new BusinessException(UNKNOWN_ERROR);
        }

        final List<User> users = new ArrayList<>();
        users.add(user);
        defaultRole.setUsers(users);

        this.roleDao.save(defaultRole);
        this.passwordResetService.sendVerificationEmail(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticationResponse refreshToken(final RefreshRequest req) {
        final String newAccessToken = this.jwtService.refreshAccessToken(req.getRefreshToken());
        final String tokenType = "Bearer";
        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(req.getRefreshToken())
                .tokenType(tokenType)
                .build();
    }

    @Override
    @Transactional(timeout = 30)
    public void logout(final String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) return;
        final String token = authorizationHeader.substring(7);
        final String jti = this.jwtService.extractJti(token);
        if (jti != null) {
            final long ttl = this.jwtService.extractRemainingTtlSeconds(token);
            this.tokenDenylist.add(jti, ttl);
            log.debug("Token revoked: jti={}, ttl={}s", jti, ttl);
        }
    }

    private void checkPasswords(final String password, final String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new BusinessException(PASSWORD_MISMATCH);
        }
    }

    private String extractConstraintName(final DataIntegrityViolationException e) {
        Throwable cause = e.getCause();
        while (cause != null) {
            if (cause instanceof ConstraintViolationException) {
                return ((ConstraintViolationException) cause).getConstraintName();
            }
            cause = cause.getCause();
        }
        return null;
    }

    @Override
    @Transactional(timeout = 30)
    public AuthenticationResponse googleSignIn(final GoogleAuthRequest request) {
        final GoogleUserInfo googleUser = this.googleTokenVerifier.verify(request.getIdToken());

        final User user = this.userDao.findByEmail(googleUser.email())
                .orElseGet(() -> provisionGoogleUser(googleUser));

        final String accessToken = this.jwtService.generateAccessToken(user.getUsername());
        final String refreshToken = this.jwtService.generateRefreshToken(user.getUsername());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    private User provisionGoogleUser(final GoogleUserInfo googleUser) {
        if (!this.autoCreateUser) {
            throw new BusinessException(USER_NOT_FOUND);
        }
        final Role defaultRole = this.roleDao.findByName(this.googleDefaultRole)
                .orElseGet(() -> this.roleDao.findAll().stream()
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("No roles configured in the system")));

        final User user = User.builder()
                .firstName(googleUser.firstName().isBlank() ? googleUser.email().split("@")[0] : googleUser.firstName())
                .lastName(googleUser.lastName().isBlank() ? "" : googleUser.lastName())
                .email(googleUser.email())
                .password(this.passwordEncoder.encode(UUID.randomUUID().toString()))
                .enabled(true)
                .locked(false)
                .credentialsExpired(false)
                .emailVerified(googleUser.emailVerified())
                .authProvider(AuthProvider.GOOGLE)
                .providerSubject(googleUser.sub())
                .build();

        final List<Role> roles = new ArrayList<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        log.debug("Auto-provisioning Google user {}", googleUser.email());
        return this.userDao.save(user);
    }
}
