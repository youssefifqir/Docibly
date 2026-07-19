package com.docibly.dms.ws.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Verify email address using the token received by email")
public class VerifyEmailRequest {

    @NotBlank
    @Schema(description = "Token received via verification email")
    private String token;
}
