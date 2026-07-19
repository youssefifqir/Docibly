package com.docibly.dms.ws.dto.tag.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTagRequest {

    @Size(max = 500, message = "name must not exceed 500 characters")
    private String name;
    @Size(max = 500, message = "slug must not exceed 500 characters")
    private String slug;
    @Size(max = 500, message = "color must not exceed 500 characters")
    private String color;
    @Min(value = 0, message = "usageCount must be positive")
    private Integer usageCount;
}

