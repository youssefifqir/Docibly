package com.docibly.dms.ws.dto.folder.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderResponse {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String name;
    private String description;
    private String color;
    private String iconName;
    private Boolean isShared;
    private Integer documentCount;
    private Long totalSizeBytes;
    private Long parentFolderId;
    private String parentFolderRef;
    private Long organizationId;
    private String organizationRef;
}

