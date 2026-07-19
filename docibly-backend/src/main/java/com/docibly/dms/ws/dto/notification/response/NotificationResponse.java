package com.docibly.dms.ws.dto.notification.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import com.docibly.dms.bean.core.enums.NotificationType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private String ref;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private NotificationType type;
    private String title;
    private String message;
    private Boolean read;
    private LocalDateTime readAt;
    private String targetUrl;
    private String relatedEntityType;
    private String relatedEntityId;
    private String recipientId;
}

