package com.docibly.dms.ws.dto.document.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDocumentCommentRequest {

    @Size(max = 500, message = "content must not exceed 500 characters")
    private String content;
    private Boolean isResolved;
    private LocalDateTime resolvedAt;
    @Min(value = 0, message = "pageNumber must be positive")
    private Integer pageNumber;
    @DecimalMin(value = "0.0", inclusive = true, message = "positionX must be positive")
    private BigDecimal positionX;
    @DecimalMin(value = "0.0", inclusive = true, message = "positionY must be positive")
    private BigDecimal positionY;
    private Boolean isEdited;
    private LocalDateTime editedAt;
}

