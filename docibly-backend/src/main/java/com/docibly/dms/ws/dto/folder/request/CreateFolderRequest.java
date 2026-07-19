package com.docibly.dms.ws.dto.folder.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFolderRequest {

    @NotBlank(message = "name is required")
    @Size(max = 500, message = "name must not exceed 500 characters")
    private String name;
    @Size(max = 500, message = "description must not exceed 500 characters")
    private String description;
    @Size(max = 500, message = "color must not exceed 500 characters")
    private String color;
    @Size(max = 500, message = "iconName must not exceed 500 characters")
    private String iconName;
    private Boolean isShared;
    @Min(value = 0, message = "documentCount must be positive")
    private Integer documentCount;
    @Min(value = 0, message = "totalSizeBytes must be positive")
    private Long totalSizeBytes;
}

