package com.docibly.dms.ws.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleAuthRequest {

    @NotBlank(message = "VALIDATION.GOOGLE_AUTH.ID_TOKEN.NOT_BLANK")
    @Schema(description = "Google ID token obtained from the frontend Google Sign-In flow")
    private String idToken;
}
