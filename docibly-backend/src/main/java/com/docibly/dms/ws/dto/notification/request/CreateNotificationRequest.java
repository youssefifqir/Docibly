package com.docibly.dms.ws.dto.notification.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.NotificationType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest {

    private NotificationType type;
    @NotBlank(message = "title is required")
    @Size(max = 500, message = "title must not exceed 500 characters")
    private String title;
    @NotBlank(message = "message is required")
    @Size(max = 500, message = "message must not exceed 500 characters")
    private String message;
    private Boolean read;
    private LocalDateTime readAt;
    @Size(max = 500, message = "targetUrl must not exceed 500 characters")
    private String targetUrl;
    @Size(max = 500, message = "relatedEntityType must not exceed 500 characters")
    private String relatedEntityType;
    @Size(max = 500, message = "relatedEntityId must not exceed 500 characters")
    private String relatedEntityId;
}

