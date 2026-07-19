package com.docibly.dms.service.impl.auditlog;

import com.docibly.dms.bean.core.auditlog.AuditLog;
import com.docibly.dms.dao.criteria.core.auditlog.AuditLogCriteria;
import com.docibly.dms.dao.facade.core.auditlog.AuditLogDao;
import com.docibly.dms.dao.specification.core.auditlog.AuditLogSpecification;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.auditlog.AuditLogService;
import com.docibly.dms.service.facade.auditlog.AuditLogger;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import com.docibly.dms.bean.core.enums.MemberRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import com.docibly.dms.common.event.EntityEvent;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final OrgAuthorizationService orgAuth;

    public AuditLogServiceImpl(AuditLogDao dao, ApplicationEventPublisher eventPublisher,
                                OrgAuthorizationService orgAuth) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.orgAuth = orgAuth;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_auditlog", key = "'list'")
    @PreAuthorize("hasPermission(null, 'AuditLog', 'READ')")
    public List<AuditLog> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_auditlog", key = "#id")
    @PreAuthorize("hasPermission(#id, 'AuditLog', 'READ')")
    public Optional<AuditLog> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_auditlog", allEntries = true)
    public AuditLog save(AuditLog entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(AuditLog entity) {
        if (entity == null) return;

    }

    private void validateDeletionAllowed(AuditLog entity) {
        if (entity == null) return;

    }

    private void prepareForDeletion(AuditLog entity) {
        if (entity == null) return;

    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_auditlog", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'AuditLog', 'DELETE')")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("AuditLog", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_auditlog", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'AuditLog', 'DELETE')")
    public Optional<AuditLog> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("AuditLog", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_auditlog", allEntries = true)
    @PreAuthorize("hasPermission(null, 'AuditLog', 'CREATE')")
    public AuditLog create(AuditLog t) { 
        AuditLog result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("AuditLog", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_auditlog", allEntries = true)
    @PreAuthorize("hasPermission(#t.id, 'AuditLog', 'UPDATE')")
    public AuditLog update(AuditLog t) { 
        if (t == null || t.getId() == null) return null;
        AuditLog existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        AuditLog result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("AuditLog", result));
        }
        return result;
    }

    private void mergeEntityData(AuditLog existing, AuditLog updated) {
        if (existing == null || updated == null) return;

        if (updated.getAction() != null) {
            existing.setAction(updated.getAction());
        }
        if (updated.getActorUserId() != null) {
            existing.setActorUserId(updated.getActorUserId().isEmpty() ? null : updated.getActorUserId());
        }
        if (updated.getActorEmail() != null) {
            existing.setActorEmail(updated.getActorEmail().isEmpty() ? null : updated.getActorEmail());
        }
        if (updated.getTargetEntityType() != null) {
            existing.setTargetEntityType(updated.getTargetEntityType().isEmpty() ? null : updated.getTargetEntityType());
        }
        if (updated.getTargetEntityId() != null) {
            existing.setTargetEntityId(updated.getTargetEntityId().isEmpty() ? null : updated.getTargetEntityId());
        }
        if (updated.getOrganizationId() != null) {
            existing.setOrganizationId(updated.getOrganizationId());
        }
        if (updated.getMetadata() != null) {
            existing.setMetadata(updated.getMetadata().isEmpty() ? null : updated.getMetadata());
        }
        if (updated.getIpAddress() != null) {
            existing.setIpAddress(updated.getIpAddress().isEmpty() ? null : updated.getIpAddress());
        }
        if (updated.getUserAgent() != null) {
            existing.setUserAgent(updated.getUserAgent().isEmpty() ? null : updated.getUserAgent());
        }

        // Handle relationships
    }

    @Override
    @Transactional(timeout = 30)
    public List<AuditLog> update(List<AuditLog> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<AuditLog> result = new ArrayList<>();
        for (AuditLog entity : ts) {
            if (entity.getId() != null) {
                AuditLog updated = update(entity);
                if (updated != null) {
                    result.add(updated);
                }
            } else if (createIfNotExist) {
                result.add(create(entity));
            }
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    public AuditLog findOrSave(AuditLog t) { 
        if (t == null) return null;
        
        AuditLog existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLog findByReferenceEntity(AuditLog t) { 
        if (t == null) return null;
        
        if (t.getId() != null) {
            return findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            return findByRef(t.getRef());
        }
        
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLog findWithAssociatedLists(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByCriteria(AuditLogCriteria criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return findAll();
        }
        return dao.findAll(new AuditLogSpecification(criteria));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> findPaginatedByCriteria(AuditLogCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<AuditLog> spec = (criteria == null || criteria.isEmpty()) ? null : new AuditLogSpecification(criteria);
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(AuditLogCriteria criteria) {
        Specification<AuditLog> spec = (criteria == null || criteria.isEmpty()) ? null : new AuditLogSpecification(criteria);
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<AuditLog> delete(List<AuditLog> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<AuditLog> deleted = new ArrayList<>();
        for (AuditLog entity : ts) {
            if (entity != null && entity.getId() != null) {
                findById(entity.getId()).ifPresent(e -> {
                    e.setDeletedAt(LocalDateTime.now());
                    dao.save(e);
                    deleted.add(e);
                });
            }
        }
        return deleted;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getOrgAuditLogs(Long organizationId, Pageable pageable) {
        orgAuth.requireRole(organizationId, MemberRole.VIEWER);
        return dao.findByOrganizationId(organizationId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLog findByRef(String ref) { 
        if (ref == null || ref.trim().isEmpty()) return null;
        return dao.findByRef(ref); 
    }
}

