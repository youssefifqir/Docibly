package com.docibly.dms.ws.dto.document.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import com.docibly.dms.bean.core.enums.OcrStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocumentVersionRequest {

    @NotNull(message = "versionNumber is required")
    @Min(value = 0, message = "versionNumber must be positive")
    private Integer versionNumber;
    @Size(max = 500, message = "label must not exceed 500 characters")
    private String label;
    @Size(max = 500, message = "changeNote must not exceed 500 characters")
    private String changeNote;
    @NotBlank(message = "originalFilename is required")
    @Size(max = 500, message = "originalFilename must not exceed 500 characters")
    private String originalFilename;
    @NotBlank(message = "storedFilename is required")
    @Size(max = 500, message = "storedFilename must not exceed 500 characters")
    private String storedFilename;
    @NotNull(message = "fileSizeBytes is required")
    @Min(value = 0, message = "fileSizeBytes must be positive")
    private Long fileSizeBytes;
    @NotBlank(message = "mimeType is required")
    @Size(max = 500, message = "mimeType must not exceed 500 characters")
    private String mimeType;
    private Boolean isCurrentVersion;
    private OcrStatus ocrStatus;
    @Size(max = 500, message = "ocrText must not exceed 500 characters")
    private String ocrText;
}
