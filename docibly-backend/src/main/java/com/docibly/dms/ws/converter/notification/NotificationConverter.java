package com.docibly.dms.ws.converter.notification;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.notification.Notification;
import com.docibly.dms.ws.dto.notification.request.CreateNotificationRequest;
import com.docibly.dms.ws.dto.notification.request.UpdateNotificationRequest;
import com.docibly.dms.ws.dto.notification.response.NotificationResponse;

@Component
public class NotificationConverter {

    public NotificationResponse toResponse(Notification entity) {
        if (entity == null) return null;
        NotificationResponse response = new NotificationResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setType(entity.getType());
        response.setTitle(entity.getTitle());
        response.setMessage(entity.getMessage());
        response.setRead(entity.getRead());
        response.setReadAt(entity.getReadAt());
        response.setTargetUrl(entity.getTargetUrl());
        response.setRelatedEntityType(entity.getRelatedEntityType());
        response.setRelatedEntityId(entity.getRelatedEntityId());
        if (entity.getRecipient() != null) {
            response.setRecipientId(entity.getRecipient().getId());
        }
        return response;
    }

    public Notification toEntity(CreateNotificationRequest request) {
        if (request == null) return null;
        Notification entity = new Notification();
        entity.setType(request.getType());
        entity.setTitle(request.getTitle());
        entity.setMessage(request.getMessage());
        entity.setRead(request.getRead());
        entity.setReadAt(request.getReadAt());
        entity.setTargetUrl(request.getTargetUrl());
        entity.setRelatedEntityType(request.getRelatedEntityType());
        entity.setRelatedEntityId(request.getRelatedEntityId());
        return entity;
    }

    public Notification toEntity(UpdateNotificationRequest request) {
        if (request == null) return null;
        Notification entity = new Notification();
        entity.setType(request.getType());
        entity.setTitle(request.getTitle());
        entity.setMessage(request.getMessage());
        entity.setRead(request.getRead());
        entity.setReadAt(request.getReadAt());
        entity.setTargetUrl(request.getTargetUrl());
        entity.setRelatedEntityType(request.getRelatedEntityType());
        entity.setRelatedEntityId(request.getRelatedEntityId());
        return entity;
    }
}

