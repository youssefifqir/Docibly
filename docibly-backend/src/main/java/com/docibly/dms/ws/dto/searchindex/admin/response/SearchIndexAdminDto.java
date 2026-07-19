package com.docibly.dms.ws.dto.searchindex.admin.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.DocumentVisibility;

/**
 * Response DTO for SearchIndex - Admin view.
 * Visible to: ADMIN
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchIndexAdminDto {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Long documentId;
    private String documentTitle;
    private String fullText;
    private String ocrText;
    private String tags;
    private String mimeType;
    private Long organizationId;
    private String ownerId;
    private DocumentVisibility visibility;
    private LocalDateTime indexedAt;
}
