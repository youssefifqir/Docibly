package com.docibly.dms.ws.controller.auth;

import com.docibly.dms.service.facade.security.AuthenticationService;
import com.docibly.dms.ws.dto.auth.AuthenticationRequest;
import com.docibly.dms.ws.dto.auth.RefreshRequest;
import com.docibly.dms.ws.dto.auth.RegistrationRequest;
import com.docibly.dms.ws.dto.auth.AuthenticationResponse;
import com.docibly.dms.ws.dto.auth.GoogleAuthRequest;
import com.docibly.dms.service.facade.email.PasswordResetService;
import com.docibly.dms.ws.dto.auth.ForgotPasswordRequest;
import com.docibly.dms.ws.dto.auth.ResetPasswordRequest;
import com.docibly.dms.ws.dto.auth.VerifyEmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<Void> register(@Valid @RequestBody final RegistrationRequest request) {
        this.authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT tokens")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody final AuthenticationRequest request) {
        return ResponseEntity.ok(this.authenticationService.login(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<AuthenticationResponse> refresh(@Valid @RequestBody final RefreshRequest request) {
        return ResponseEntity.ok(this.authenticationService.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Revoke the current access token", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) final String authHeader) {
        this.authenticationService.logout(authHeader);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/google")
    @Operation(summary = "Authenticate via Google ID token and return JWT tokens")
    public ResponseEntity<AuthenticationResponse> googleSignIn(@Valid @RequestBody final GoogleAuthRequest request) {
        return ResponseEntity.ok(this.authenticationService.googleSignIn(request));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Request a password reset email")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody final ForgotPasswordRequest request) {
        this.passwordResetService.initiateForgotPassword(request.getEmail());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password using the token received by email")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody final ResetPasswordRequest request) {
        this.passwordResetService.resetPassword(request.getToken(), request.getNewPassword(), request.getConfirmNewPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-email")
    @Operation(summary = "Verify email address using the token received by email")
    public ResponseEntity<Void> verifyEmail(@Valid @RequestBody final VerifyEmailRequest request) {
        this.passwordResetService.verifyEmail(request.getToken());
        return ResponseEntity.noContent().build();
    }
}
