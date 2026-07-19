package com.docibly.dms.service.impl.notification;

import com.docibly.dms.bean.core.notification.Notification;
import com.docibly.dms.dao.criteria.core.notification.NotificationCriteria;
import com.docibly.dms.dao.facade.core.notification.NotificationDao;
import com.docibly.dms.dao.specification.core.notification.NotificationSpecification;
import com.docibly.dms.service.facade.notification.NotificationService;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.security.UserDao;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.docibly.dms.config.security.authorization.EntityPermissions;
import com.docibly.dms.config.security.authorization.RolePermissions;
import jakarta.persistence.criteria.Predicate;
import java.util.Map;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<String, EntityPermissions> permissionRegistry;
    private final UserDao userDao;

    public NotificationServiceImpl(NotificationDao dao, ApplicationEventPublisher eventPublisher, Map<String, EntityPermissions> permissionRegistry, UserDao userDao) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.permissionRegistry = permissionRegistry;
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'Notification', 'READ')")
    public List<Notification> findAll() {
        Specification<Notification> spec = buildOwnershipSpec("Notification");
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_notification", key = "#id")
    @PreAuthorize("hasPermission(#id, 'Notification', 'READ')")
    public Optional<Notification> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_notification", allEntries = true)
    public Notification save(Notification entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(Notification entity) {
        if (entity == null) return;

        // Validate ManyToOne to User (security entity with String UUID id)
        if (entity.getRecipient() != null) {
            User recipientEntity = entity.getRecipient();
            if (recipientEntity.getId() != null) {
                User existingRecipient = this.userDao.findById(recipientEntity.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Recipient with id " + recipientEntity.getId() + " does not exist"));
                entity.setRecipient(existingRecipient);
            } else {
                throw new IllegalArgumentException("Recipient must be referenced by id");
            }
        }
    }

    private void validateDeletionAllowed(Notification entity) {
        if (entity == null) return;

    }

    private void prepareForDeletion(Notification entity) {
        if (entity == null) return;

    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_notification", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'Notification', 'DELETE')")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Notification", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_notification", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'Notification', 'DELETE')")
    public Optional<Notification> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Notification", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_notification", allEntries = true)
    @PreAuthorize("hasPermission(null, 'Notification', 'CREATE')")
    public Notification create(Notification t) { 
        Notification result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("Notification", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_notification", allEntries = true)
    @PreAuthorize("hasPermission(#t.id, 'Notification', 'UPDATE')")
    public Notification update(Notification t) { 
        if (t == null || t.getId() == null) return null;
        Notification existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        Notification result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("Notification", result));
        }
        return result;
    }

    private void mergeEntityData(Notification existing, Notification updated) {
        if (existing == null || updated == null) return;

        if (updated.getType() != null) {
            existing.setType(updated.getType());
        }
        if (updated.getTitle() != null) {
            existing.setTitle(updated.getTitle().isEmpty() ? null : updated.getTitle());
        }
        if (updated.getMessage() != null) {
            existing.setMessage(updated.getMessage().isEmpty() ? null : updated.getMessage());
        }
        if (updated.getRead() != null) {
            existing.setRead(updated.getRead());
        }
        if (updated.getReadAt() != null) {
            existing.setReadAt(updated.getReadAt());
        }
        if (updated.getTargetUrl() != null) {
            existing.setTargetUrl(updated.getTargetUrl().isEmpty() ? null : updated.getTargetUrl());
        }
        if (updated.getRelatedEntityType() != null) {
            existing.setRelatedEntityType(updated.getRelatedEntityType().isEmpty() ? null : updated.getRelatedEntityType());
        }
        if (updated.getRelatedEntityId() != null) {
            existing.setRelatedEntityId(updated.getRelatedEntityId().isEmpty() ? null : updated.getRelatedEntityId());
        }

        // Handle relationships
        if (updated.getRecipient() != null) {
            existing.setRecipient(updated.getRecipient());
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<Notification> update(List<Notification> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Notification> result = new ArrayList<>();
        for (Notification entity : ts) {
            if (entity.getId() != null) {
                Notification updated = update(entity);
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
    public Notification findOrSave(Notification t) { 
        if (t == null) return null;
        
        Notification existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Notification findByReferenceEntity(Notification t) { 
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
    public Notification findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> findByCriteria(NotificationCriteria criteria) {
        Specification<Notification> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new NotificationSpecification(criteria),
                buildOwnershipSpec("Notification")
        );
        return dao.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Notification> findPaginatedByCriteria(NotificationCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<Notification> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new NotificationSpecification(criteria),
                buildOwnershipSpec("Notification")
        );
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(NotificationCriteria criteria) {
        Specification<Notification> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new NotificationSpecification(criteria),
                buildOwnershipSpec("Notification")
        );
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<Notification> delete(List<Notification> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Notification> deleted = new ArrayList<>();
        for (Notification entity : ts) {
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
    public Notification findByRef(String ref) { 
        if (ref == null || ref.trim().isEmpty()) return null;
        return dao.findByRef(ref); 
    }


    /**
     * Builds an ownership/scoped filter Specification for list queries.
     * <ul>
     *   <li>Returns null if the current user has {@code all:READ} via any role (full table access).</li>
     *   <li>OR-combines all qualifying {@code scoped} specs (additive — more roles = more data visible).</li>
     *   <li>OR-in a {@code createdBy == userId} predicate if any role grants {@code own:READ}.</li>
     * </ul>
     */
    private Specification<Notification> buildOwnershipSpec(String entityName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            return null;
        }
        User user = (User) auth.getPrincipal();
        EntityPermissions ep = permissionRegistry.get(entityName);
        if (ep == null) {
            return null;
        }

        // 1. All:READ via any role → full table, no filter
        for (var role : user.getRoles()) {
            String roleName = role.getName().replace("ROLE_", "");
            RolePermissions rp = ep.getRolePermissions().get(roleName);
            if (rp != null && rp.getAllOperations().contains("READ")) {
                return null;
            }
        }

        // 2. Collect scoped specs → OR-combine all qualifying scoped predicates
        boolean hasOwnRead = false;
        Specification<Notification> combined = null;

        for (var role : user.getRoles()) {
            String roleName = role.getName().replace("ROLE_", "");
            RolePermissions rp = ep.getRolePermissions().get(roleName);
            if (rp == null) continue;

            // Scoped READ: predicate comparing entity.{scopeField} to user.{scopeField}

            // Own READ: track for additive fallback
            if (rp.getOwnOperations().contains("READ")) {
                hasOwnRead = true;
            }
        }

        // 3. Own spec as additive fallback — createdBy compared to user UUID
        if (hasOwnRead) {
            Specification<Notification> ownSpec = (root, query, cb) ->
                cb.equal(root.get("createdBy"), user.getId());
            combined = (combined == null) ? ownSpec : combined.or(ownSpec);
        }

        return combined;
    }

    /**
     * Combines two Specifications with AND logic, handling null gracefully.
     */
    private Specification<Notification> combineSpecs(Specification<Notification> criteriaSpec,
                                                       Specification<Notification> ownershipSpec) {
        if (criteriaSpec == null && ownershipSpec == null) return null;
        if (criteriaSpec == null) return ownershipSpec;
        if (ownershipSpec == null) return criteriaSpec;
        return criteriaSpec.and(ownershipSpec);
    }
}

