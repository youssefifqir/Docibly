package com.docibly.dms.ws.dto.document.admin.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.OcrStatus;

/**
 * Response DTO for DocumentVersion - Admin view.
 * Visible to: ADMIN
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVersionAdminDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer versionNumber;
    private String label;
    private String changeNote;
    private String originalFilename;
    private String storedFilename;
    private String storageKey;
    private Long fileSizeBytes;
    private String mimeType;
    private String checksum;
    private Boolean isCurrentVersion;
    private OcrStatus ocrStatus;
    private String ocrText;
}
