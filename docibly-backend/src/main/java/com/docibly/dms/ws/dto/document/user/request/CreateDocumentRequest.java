package com.docibly.dms.ws.dto.document.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.DocumentStatus;
import com.docibly.dms.bean.core.enums.DocumentVisibility;
import com.docibly.dms.bean.core.enums.OcrStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocumentRequest {

    @NotBlank(message = "title is required")
    @Size(max = 500, message = "title must not exceed 500 characters")
    private String title;
    @Size(max = 500, message = "description must not exceed 500 characters")
    private String description;
    @NotBlank(message = "originalFilename is required")
    @Size(max = 500, message = "originalFilename must not exceed 500 characters")
    private String originalFilename;
    @NotBlank(message = "storedFilename is required")
    @Size(max = 500, message = "storedFilename must not exceed 500 characters")
    private String storedFilename;
    @NotBlank(message = "mimeType is required")
    @Size(max = 500, message = "mimeType must not exceed 500 characters")
    private String mimeType;
    @NotNull(message = "fileSizeBytes is required")
    @Min(value = 0, message = "fileSizeBytes must be positive")
    private Long fileSizeBytes;
    private DocumentStatus status;
    private DocumentVisibility visibility;
    @Min(value = 0, message = "currentVersionNumber must be positive")
    private Integer currentVersionNumber;
    @Min(value = 0, message = "downloadCount must be positive")
    private Integer downloadCount;
    @Min(value = 0, message = "viewCount must be positive")
    private Integer viewCount;
    private Boolean isPasswordProtected;
    private LocalDateTime expiresAt;
    private LocalDateTime archivedAt;
    private OcrStatus ocrStatus;
    @Size(max = 500, message = "ocrText must not exceed 500 characters")
    private String ocrText;
    private LocalDateTime ocrProcessedAt;
    @Size(max = 500, message = "ocrLanguage must not exceed 500 characters")
    private String ocrLanguage;
    @DecimalMin(value = "0.0", inclusive = true, message = "ocrConfidenceScore must be positive")
    private BigDecimal ocrConfidenceScore;
}
