package com.docibly.dms.service.impl.notification;

import com.docibly.dms.bean.core.enums.NotificationType;
import com.docibly.dms.bean.core.notification.Notification;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.notification.NotificationDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.notification.NotificationFiringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationFiringServiceImpl implements NotificationFiringService {

    private final NotificationDao notificationDao;
    private final UserDao userDao;

    @Override
    @Transactional
    public Notification fire(String recipientUserId, NotificationType type, String title,
                              String message, String targetUrl, String relatedEntityType, String relatedEntityId) {
        User recipient = userDao.findById(recipientUserId)
                .orElse(null);
        if (recipient == null) {
            log.warn("Skipping notification: recipient user {} not found", recipientUserId);
            return null;
        }

        Notification notification = Notification.builder()
                .type(type)
                .title(title)
                .message(message)
                .read(false)
                .targetUrl(targetUrl)
                .relatedEntityType(relatedEntityType)
                .relatedEntityId(relatedEntityId)
                .recipient(recipient)
                .build();
        notification = notificationDao.save(notification);

        log.debug("Notification fired: userId={}, type={}, title={}", recipientUserId, type, title);
        return notification;
    }

    @Override
    @Transactional
    public Notification inviteReceived(String recipientUserId, String orgName, String invitedByEmail) {
        return fire(recipientUserId, NotificationType.INVITATION_RECEIVED,
                "Invitation to " + orgName,
                "You've been invited to join " + orgName + " by " + invitedByEmail,
                null, "Organization", null);
    }

    @Override
    @Transactional
    public Notification memberJoined(String orgOwnerUserId, String orgName, String memberEmail) {
        return fire(orgOwnerUserId, NotificationType.MEMBER_JOINED,
                "New member joined " + orgName,
                memberEmail + " has joined your organization " + orgName,
                null, "OrganizationMember", null);
    }

    @Override
    @Transactional
    public Notification shareCreated(String recipientUserId, String docTitle, String sharerName, String shareToken) {
        return fire(recipientUserId, NotificationType.DOCUMENT_SHARED,
                "Document shared: " + docTitle,
                sharerName + " shared \"" + docTitle + "\" with you",
                "/api/v1/public/shares/" + shareToken, "DocumentShare", shareToken);
    }

    @Override
    @Transactional
    public Notification commentAdded(String docOwnerUserId, String docTitle, String commenterName, Long documentId) {
        return fire(docOwnerUserId, NotificationType.COMMENT_ADDED,
                "New comment on " + docTitle,
                commenterName + " commented on \"" + docTitle + "\"",
                "/api/v1/user/documents/" + documentId, "Document", String.valueOf(documentId));
    }

    @Override
    @Transactional
    public Notification ocrReady(String userId, String docTitle, Long documentId) {
        return fire(userId, NotificationType.OCR_READY,
                "OCR complete: " + docTitle,
                "Text extraction finished for \"" + docTitle + "\"",
                "/api/v1/user/documents/" + documentId, "Document", String.valueOf(documentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getMyNotifications(String userId) {
        return notificationDao.findByRecipient_IdOrderByCreatedDateDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(String userId) {
        return notificationDao.countByRecipient_IdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public Notification markRead(Long notificationId, String userId) {
        Notification notification = notificationDao.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND));
        if (!notification.getRecipient().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOTIFICATION_NOT_OWNER);
        }
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        return notificationDao.save(notification);
    }

    @Override
    @Transactional
    public void markAllRead(String userId) {
        List<Notification> unread = notificationDao.findByRecipient_IdOrderByCreatedDateDesc(userId)
                .stream().filter(n -> !Boolean.TRUE.equals(n.getRead())).toList();
        unread.forEach(n -> {
            n.setRead(true);
            n.setReadAt(LocalDateTime.now());
        });
        notificationDao.saveAll(unread);
    }
}
