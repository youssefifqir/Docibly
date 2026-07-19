package com.docibly.dms.service.impl.organization;

import com.docibly.dms.bean.core.organization.OrganizationRequest;
import com.docibly.dms.dao.criteria.core.organization.OrganizationRequestCriteria;
import com.docibly.dms.dao.facade.core.organization.OrganizationRequestDao;
import com.docibly.dms.dao.specification.core.organization.OrganizationRequestSpecification;
import com.docibly.dms.service.facade.organization.OrganizationRequestService;
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
public class OrganizationRequestServiceImpl implements OrganizationRequestService {

    private final OrganizationRequestDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<String, EntityPermissions> permissionRegistry;
    private final UserDao userDao;

    public OrganizationRequestServiceImpl(OrganizationRequestDao dao, ApplicationEventPublisher eventPublisher, Map<String, EntityPermissions> permissionRegistry, UserDao userDao) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.permissionRegistry = permissionRegistry;
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'OrganizationRequest', 'READ')")
    public List<OrganizationRequest> findAll() {
        Specification<OrganizationRequest> spec = buildOwnershipSpec("OrganizationRequest");
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_organizationrequest", key = "#id")
    @PreAuthorize("hasPermission(#id, 'OrganizationRequest', 'READ')")
    public Optional<OrganizationRequest> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organizationrequest", allEntries = true)
    public OrganizationRequest save(OrganizationRequest entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(OrganizationRequest entity) {
        if (entity == null) return;

        // Validate ManyToOne to User (security entity with String UUID id)
        if (entity.getRequestedBy() != null) {
            User requestedByEntity = entity.getRequestedBy();
            if (requestedByEntity.getId() != null) {
                User existingRequestedBy = this.userDao.findById(requestedByEntity.getId())
                        .orElseThrow(() -> new IllegalArgumentException("RequestedBy with id " + requestedByEntity.getId() + " does not exist"));
                entity.setRequestedBy(existingRequestedBy);
            } else {
                throw new IllegalArgumentException("RequestedBy must be referenced by id");
            }
        }
        // Validate ManyToOne to User (security entity with String UUID id)
        if (entity.getReviewedBy() != null) {
            User reviewedByEntity = entity.getReviewedBy();
            if (reviewedByEntity.getId() != null) {
                User existingReviewedBy = this.userDao.findById(reviewedByEntity.getId())
                        .orElseThrow(() -> new IllegalArgumentException("ReviewedBy with id " + reviewedByEntity.getId() + " does not exist"));
                entity.setReviewedBy(existingReviewedBy);
            } else {
                throw new IllegalArgumentException("ReviewedBy must be referenced by id");
            }
        }
    }

    private void validateDeletionAllowed(OrganizationRequest entity) {
        if (entity == null) return;

    }

    private void prepareForDeletion(OrganizationRequest entity) {
        if (entity == null) return;

    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organizationrequest", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'OrganizationRequest', 'DELETE')")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("OrganizationRequest", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organizationrequest", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'OrganizationRequest', 'DELETE')")
    public Optional<OrganizationRequest> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("OrganizationRequest", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organizationrequest", allEntries = true)
    @PreAuthorize("hasPermission(null, 'OrganizationRequest', 'CREATE')")
    public OrganizationRequest create(OrganizationRequest t) { 
        OrganizationRequest result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("OrganizationRequest", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organizationrequest", allEntries = true)
    @PreAuthorize("hasPermission(#t.id, 'OrganizationRequest', 'UPDATE')")
    public OrganizationRequest update(OrganizationRequest t) { 
        if (t == null || t.getId() == null) return null;
        OrganizationRequest existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        OrganizationRequest result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("OrganizationRequest", result));
        }
        return result;
    }

    private void mergeEntityData(OrganizationRequest existing, OrganizationRequest updated) {
        if (existing == null || updated == null) return;

        if (updated.getRequestedName() != null) {
            existing.setRequestedName(updated.getRequestedName().isEmpty() ? null : updated.getRequestedName());
        }
        if (updated.getRequestedSlug() != null) {
            existing.setRequestedSlug(updated.getRequestedSlug().isEmpty() ? null : updated.getRequestedSlug());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription().isEmpty() ? null : updated.getDescription());
        }
        if (updated.getIntendedUse() != null) {
            existing.setIntendedUse(updated.getIntendedUse().isEmpty() ? null : updated.getIntendedUse());
        }
        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
        if (updated.getReviewedAt() != null) {
            existing.setReviewedAt(updated.getReviewedAt());
        }
        if (updated.getRejectionReason() != null) {
            existing.setRejectionReason(updated.getRejectionReason().isEmpty() ? null : updated.getRejectionReason());
        }
        if (updated.getCreatedOrganizationId() != null) {
            existing.setCreatedOrganizationId(updated.getCreatedOrganizationId());
        }

        // Handle relationships
        if (updated.getRequestedBy() != null) {
            existing.setRequestedBy(updated.getRequestedBy());
        }
        if (updated.getReviewedBy() != null) {
            existing.setReviewedBy(updated.getReviewedBy());
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<OrganizationRequest> update(List<OrganizationRequest> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<OrganizationRequest> result = new ArrayList<>();
        for (OrganizationRequest entity : ts) {
            if (entity.getId() != null) {
                OrganizationRequest updated = update(entity);
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
    public OrganizationRequest findOrSave(OrganizationRequest t) { 
        if (t == null) return null;
        
        OrganizationRequest existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationRequest findByReferenceEntity(OrganizationRequest t) { 
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
    public OrganizationRequest findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationRequest> findByCriteria(OrganizationRequestCriteria criteria) {
        Specification<OrganizationRequest> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new OrganizationRequestSpecification(criteria),
                buildOwnershipSpec("OrganizationRequest")
        );
        return dao.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationRequest> findPaginatedByCriteria(OrganizationRequestCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<OrganizationRequest> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new OrganizationRequestSpecification(criteria),
                buildOwnershipSpec("OrganizationRequest")
        );
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(OrganizationRequestCriteria criteria) {
        Specification<OrganizationRequest> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new OrganizationRequestSpecification(criteria),
                buildOwnershipSpec("OrganizationRequest")
        );
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<OrganizationRequest> delete(List<OrganizationRequest> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<OrganizationRequest> deleted = new ArrayList<>();
        for (OrganizationRequest entity : ts) {
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
    public OrganizationRequest findByRef(String ref) { 
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
    private Specification<OrganizationRequest> buildOwnershipSpec(String entityName) {
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
        Specification<OrganizationRequest> combined = null;

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
            Specification<OrganizationRequest> ownSpec = (root, query, cb) ->
                cb.equal(root.get("createdBy"), user.getId());
            combined = (combined == null) ? ownSpec : combined.or(ownSpec);
        }

        return combined;
    }

    /**
     * Combines two Specifications with AND logic, handling null gracefully.
     */
    private Specification<OrganizationRequest> combineSpecs(Specification<OrganizationRequest> criteriaSpec,
                                                       Specification<OrganizationRequest> ownershipSpec) {
        if (criteriaSpec == null && ownershipSpec == null) return null;
        if (criteriaSpec == null) return ownershipSpec;
        if (ownershipSpec == null) return criteriaSpec;
        return criteriaSpec.and(ownershipSpec);
    }
}

