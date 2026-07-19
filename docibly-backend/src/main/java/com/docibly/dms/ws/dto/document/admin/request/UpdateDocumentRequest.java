package com.docibly.dms.ws.dto.document.admin.request;

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
public class UpdateDocumentRequest {

    @Size(max = 500, message = "title must not exceed 500 characters")
    private String title;
    @Size(max = 500, message = "description must not exceed 500 characters")
    private String description;
    @Size(max = 500, message = "originalFilename must not exceed 500 characters")
    private String originalFilename;
    @Size(max = 500, message = "storedFilename must not exceed 500 characters")
    private String storedFilename;
    @Size(max = 500, message = "mimeType must not exceed 500 characters")
    private String mimeType;
    @Min(value = 0, message = "fileSizeBytes must be positive")
    private Long fileSizeBytes;
    @Size(max = 500, message = "storageBucket must not exceed 500 characters")
    private String storageBucket;
    @Size(max = 500, message = "storageKey must not exceed 500 characters")
    private String storageKey;
    private DocumentStatus status;
    private DocumentVisibility visibility;
    @Min(value = 0, message = "currentVersionNumber must be positive")
    private Integer currentVersionNumber;
    @Min(value = 0, message = "downloadCount must be positive")
    private Integer downloadCount;
    @Min(value = 0, message = "viewCount must be positive")
    private Integer viewCount;
    private Boolean isPasswordProtected;
    @Size(max = 500, message = "passwordHash must not exceed 500 characters")
    private String passwordHash;
    private LocalDateTime expiresAt;
    private LocalDateTime archivedAt;
    @Size(max = 500, message = "checksum must not exceed 500 characters")
    private String checksum;
    private OcrStatus ocrStatus;
    @Size(max = 500, message = "ocrText must not exceed 500 characters")
    private String ocrText;
    private LocalDateTime ocrProcessedAt;
    @Size(max = 500, message = "ocrLanguage must not exceed 500 characters")
    private String ocrLanguage;
    @DecimalMin(value = "0.0", inclusive = true, message = "ocrConfidenceScore must be positive")
    private BigDecimal ocrConfidenceScore;
}
