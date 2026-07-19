package com.docibly.dms.ws.dto.storage;

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
@Schema(description = "Request body to obtain a presigned upload URL")
public class PresignedUrlRequest {

    @NotBlank(message = "VALIDATION.STORAGE.FILE_NAME.NOT_BLANK")
    @Schema(description = "Original filename including extension", example = "avatar.jpg")
    private String fileName;

    @NotBlank(message = "VALIDATION.STORAGE.CONTENT_TYPE.NOT_BLANK")
    @Schema(description = "MIME type of the file to be uploaded", example = "image/jpeg")
    private String contentType;

    @Schema(description = "Logical folder prefix (e.g. 'avatars', 'documents')", example = "avatars")
    private String folder;
}
