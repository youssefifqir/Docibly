package com.docibly.dms.ws.dto.searchindex.user.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import com.docibly.dms.bean.core.enums.DocumentVisibility;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSearchIndexRequest {

    @Min(value = 0, message = "documentId must be positive")
    private Long documentId;
    @Size(max = 500, message = "documentTitle must not exceed 500 characters")
    private String documentTitle;
    @Size(max = 500, message = "fullText must not exceed 500 characters")
    private String fullText;
    @Size(max = 500, message = "ocrText must not exceed 500 characters")
    private String ocrText;
    @Size(max = 500, message = "tags must not exceed 500 characters")
    private String tags;
    @Size(max = 500, message = "mimeType must not exceed 500 characters")
    private String mimeType;
    private DocumentVisibility visibility;
}
