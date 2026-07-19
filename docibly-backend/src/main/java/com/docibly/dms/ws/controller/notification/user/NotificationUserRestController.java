package com.docibly.dms.ws.controller.notification.user;

import com.docibly.dms.bean.core.notification.Notification;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.notification.NotificationFiringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/notifications")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class NotificationUserRestController {

    private final NotificationFiringService notificationFiringService;
    private final UserDao userDao;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications() {
        User user = getCurrentUser();
        List<Notification> notifications = notificationFiringService.getMyNotifications(user.getId());
        return ResponseEntity.ok(notifications.stream().map(this::toResponse).toList());
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        User user = getCurrentUser();
        long count = notificationFiringService.getUnreadCount(user.getId());
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markRead(@PathVariable Long id) {
        User user = getCurrentUser();
        Notification notification = notificationFiringService.markRead(id, user.getId());
        return ResponseEntity.ok(toResponse(notification));
    }

    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllRead() {
        User user = getCurrentUser();
        notificationFiringService.markAllRead(user.getId());
        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        return userDao.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(
                n.getId(), n.getType(), n.getTitle(), n.getMessage(),
                n.getRead(), n.getReadAt(), n.getCreatedDate(),
                n.getTargetUrl(), n.getRelatedEntityType(), n.getRelatedEntityId()
        );
    }

    public record NotificationResponse(
            Long id, Object type, String title, String message,
            Boolean read, java.time.LocalDateTime readAt, java.time.LocalDateTime createdAt,
            String targetUrl, String relatedEntityType, String relatedEntityId
    ) {}
}
