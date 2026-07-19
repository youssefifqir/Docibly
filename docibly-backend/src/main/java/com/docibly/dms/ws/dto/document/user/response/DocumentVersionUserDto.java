package com.docibly.dms.ws.dto.document.user.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.OcrStatus;

/**
 * Response DTO for DocumentVersion - User view.
 * Visible to: USER
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVersionUserDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Integer versionNumber;
    private String label;
    private String changeNote;
    private String originalFilename;
    private String storedFilename;
    private Long fileSizeBytes;
    private String mimeType;
    private Boolean isCurrentVersion;
    private OcrStatus ocrStatus;
    private String ocrText;
}
