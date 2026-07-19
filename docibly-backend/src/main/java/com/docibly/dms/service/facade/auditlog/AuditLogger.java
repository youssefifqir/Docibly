package com.docibly.dms.service.facade.auditlog;

import com.docibly.dms.bean.core.enums.AuditAction;

public interface AuditLogger {

    void log(AuditAction action, String targetEntityType, String targetEntityId,
             Long organizationId, String metadata);

    void log(AuditAction action, String targetEntityType, String targetEntityId,
             Long organizationId, String actorEmail, String metadata);
}
