package com.docibly.dms.service.facade.notification;

import com.docibly.dms.bean.core.enums.NotificationType;
import com.docibly.dms.bean.core.notification.Notification;

import java.util.List;

public interface NotificationFiringService {

    Notification fire(String recipientUserId, NotificationType type, String title,
                       String message, String targetUrl, String relatedEntityType, String relatedEntityId);

    Notification inviteReceived(String recipientUserId, String orgName, String invitedByEmail);

    Notification memberJoined(String orgOwnerUserId, String orgName, String memberEmail);

    Notification shareCreated(String recipientUserId, String docTitle, String sharerName, String shareToken);

    Notification commentAdded(String docOwnerUserId, String docTitle, String commenterName, Long documentId);

    Notification ocrReady(String userId, String docTitle, Long documentId);

    List<Notification> getMyNotifications(String userId);

    long getUnreadCount(String userId);

    Notification markRead(Long notificationId, String userId);

    void markAllRead(String userId);
}
