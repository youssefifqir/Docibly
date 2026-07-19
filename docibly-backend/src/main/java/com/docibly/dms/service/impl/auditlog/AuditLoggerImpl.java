package com.docibly.dms.service.impl.auditlog;

import com.docibly.dms.bean.core.auditlog.AuditLog;
import com.docibly.dms.bean.core.enums.AuditAction;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.auditlog.AuditLogDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.service.facade.auditlog.AuditLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLoggerImpl implements AuditLogger {

    private final AuditLogDao auditLogDao;
    private final UserDao userDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(AuditAction action, String targetEntityType, String targetEntityId,
                    Long organizationId, String metadata) {
        doLog(action, targetEntityType, targetEntityId, organizationId, null, metadata);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(AuditAction action, String targetEntityType, String targetEntityId,
                    Long organizationId, String actorEmail, String metadata) {
        doLog(action, targetEntityType, targetEntityId, organizationId, actorEmail, metadata);
    }

    private void doLog(AuditAction action, String targetEntityType, String targetEntityId,
                       Long organizationId, String explicitEmail, String metadata) {
        try {
            String actorUserId = null;
            String actorEmail = explicitEmail;

            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof User user) {
                actorUserId = user.getId();
                if (actorEmail == null) {
                    actorEmail = user.getEmail();
                }
                User freshUser = userDao.findById(user.getId()).orElse(null);
                if (freshUser != null) {
                    actorUserId = freshUser.getId();
                    if (actorEmail == null) {
                        actorEmail = freshUser.getEmail();
                    }
                }
            }

            String ipAddress = null;
            String userAgent = null;
            try {
                RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
                if (attrs instanceof ServletRequestAttributes servletAttrs) {
                    HttpServletRequest request = servletAttrs.getRequest();
                    ipAddress = request.getRemoteAddr();
                    userAgent = request.getHeader("User-Agent");
                }
            } catch (Exception ignored) {}

            AuditLog auditLog = AuditLog.builder()
                    .action(action)
                    .actorUserId(actorUserId)
                    .actorEmail(actorEmail)
                    .targetEntityType(targetEntityType)
                    .targetEntityId(targetEntityId)
                    .organizationId(organizationId)
                    .metadata(metadata != null && metadata.length() > 500
                            ? metadata.substring(0, 500) : metadata)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .build();
            auditLogDao.save(auditLog);

            log.debug("Audit log: action={}, entity={}/{}, orgId={}, actor={}",
                    action, targetEntityType, targetEntityId, organizationId, actorEmail);
        } catch (Exception e) {
            log.warn("Failed to write audit log: {}", e.getMessage());
        }
    }
}
