package com.docibly.dms.service.facade.auditlog;

import com.docibly.dms.bean.core.auditlog.AuditLog;
import com.docibly.dms.dao.criteria.core.auditlog.AuditLogCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface AuditLogService {

    AuditLog create(AuditLog t);
    AuditLog update(AuditLog t);
    List<AuditLog> update(List<AuditLog> ts, boolean createIfNotExist);
    Optional<AuditLog> findById(Long id);
    AuditLog save(AuditLog entity);
    void deleteById(Long id);
    Optional<AuditLog> findAndDeleteById(Long id);
    AuditLog findOrSave(AuditLog t);
    AuditLog findByReferenceEntity(AuditLog t);
    AuditLog findWithAssociatedLists(Long id);
    List<AuditLog> findAll();
    List<AuditLog> findByCriteria(AuditLogCriteria criteria);
    Page<AuditLog> findPaginatedByCriteria(AuditLogCriteria criteria, Pageable pageable);
    int getDataSize(AuditLogCriteria criteria);
    List<AuditLog> delete(List<AuditLog> ts);
    AuditLog findByRef(String ref);

    Page<AuditLog> getOrgAuditLogs(Long organizationId, Pageable pageable);
}

