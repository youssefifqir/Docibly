package com.docibly.dms.ws.dto.document.admin.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.docibly.dms.bean.core.enums.DocumentStatus;
import com.docibly.dms.bean.core.enums.DocumentVisibility;
import com.docibly.dms.bean.core.enums.OcrStatus;

/**
 * Response DTO for Document - Admin view.
 * Visible to: ADMIN
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentAdminDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String title;
    private String description;
    private String originalFilename;
    private String storedFilename;
    private String mimeType;
    private Long fileSizeBytes;
    private String storageBucket;
    private String storageKey;
    private DocumentStatus status;
    private DocumentVisibility visibility;
    private Integer currentVersionNumber;
    private Integer downloadCount;
    private Integer viewCount;
    private Boolean isPasswordProtected;
    private String passwordHash;
    private LocalDateTime expiresAt;
    private LocalDateTime archivedAt;
    private String checksum;
    private OcrStatus ocrStatus;
    private String ocrText;
    private LocalDateTime ocrProcessedAt;
    private String ocrLanguage;
    private BigDecimal ocrConfidenceScore;
}
