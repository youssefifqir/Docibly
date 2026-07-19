package com.docibly.dms.bean.core.searchindex;

import com.docibly.dms.bean.core.enums.DocumentVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** Search result shape returned by {@code GET /api/v1/search} — backed by Postgres full-text search, not a persisted entity. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentIndex {

    private Long documentId;
    private String title;
    private String description;
    private String content;
    private String originalFilename;
    private String mimeType;
    private String tags;
    private Long organizationId;
    private DocumentVisibility visibility;
    private LocalDateTime createdDate;
    private String ownerId;
}
