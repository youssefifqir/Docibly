package com.docibly.dms.ws.dto.document.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentCommentResponse {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String content;
    private Boolean isResolved;
    private LocalDateTime resolvedAt;
    private Integer pageNumber;
    private BigDecimal positionX;
    private BigDecimal positionY;
    private Boolean isEdited;
    private LocalDateTime editedAt;
    private Long documentId;
    private String documentRef;
    private Long parentCommentId;
    private String parentCommentRef;
    private String authorId;
}

