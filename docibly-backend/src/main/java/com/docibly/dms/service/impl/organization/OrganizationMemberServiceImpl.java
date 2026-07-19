package com.docibly.dms.service.impl.organization;

import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.dao.criteria.core.organization.OrganizationMemberCriteria;
import com.docibly.dms.dao.facade.core.organization.OrganizationMemberDao;
import com.docibly.dms.dao.specification.core.organization.OrganizationMemberSpecification;
import com.docibly.dms.service.facade.organization.OrganizationMemberService;
import com.docibly.dms.service.facade.organization.OrganizationService;
import com.docibly.dms.bean.core.organization.Organization;
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
public class OrganizationMemberServiceImpl implements OrganizationMemberService {

    private final OrganizationMemberDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final Map<String, EntityPermissions> permissionRegistry;
    private final UserDao userDao;
    private final OrganizationService organizationService;

    public OrganizationMemberServiceImpl(OrganizationMemberDao dao, ApplicationEventPublisher eventPublisher, Map<String, EntityPermissions> permissionRegistry, UserDao userDao, @Lazy OrganizationService organizationService) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.permissionRegistry = permissionRegistry;
        this.userDao = userDao;
        this.organizationService = organizationService;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'OrganizationMember', 'READ')")
    public List<OrganizationMember> findAll() {
        Specification<OrganizationMember> spec = buildOwnershipSpec("OrganizationMember");
        return spec != null ? dao.findAll(spec) : dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_organizationmember", key = "#id")
    @PreAuthorize("hasPermission(#id, 'OrganizationMember', 'READ')")
    public Optional<OrganizationMember> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organizationmember", allEntries = true)
    public OrganizationMember save(OrganizationMember entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(OrganizationMember entity) {
        if (entity == null) return;

        // Validate ManyToOne relationships exist
        if (entity.getOrganization() != null) {
            Organization organizationEntity = entity.getOrganization();
            if (organizationEntity.getId() != null) {
                Organization existingOrganization = organizationService.findById(organizationEntity.getId()).orElse(null);
                if (existingOrganization == null) {
                    throw new IllegalArgumentException("Organization with id " + organizationEntity.getId() + " does not exist");
                }
                entity.setOrganization(existingOrganization);
            } else if (organizationEntity.getRef() != null) {
                Organization existingOrganization = organizationService.findByRef(organizationEntity.getRef());
                if (existingOrganization == null) {
                    throw new IllegalArgumentException("Organization with ref '" + organizationEntity.getRef() + "' does not exist");
                }
                entity.setOrganization(existingOrganization);
            } else {
                throw new IllegalArgumentException("Organization must be referenced by id or ref");
            }
        }
        // Validate ManyToOne to User (security entity with String UUID id)
        if (entity.getUser() != null) {
            User userEntity = entity.getUser();
            if (userEntity.getId() != null) {
                User existingUser = this.userDao.findById(userEntity.getId())
                        .orElseThrow(() -> new IllegalArgumentException("User with id " + userEntity.getId() + " does not exist"));
                entity.setUser(existingUser);
            } else {
                throw new IllegalArgumentException("User must be referenced by id");
            }
        }
    }

    private void validateDeletionAllowed(OrganizationMember entity) {
        if (entity == null) return;

    }

    private void prepareForDeletion(OrganizationMember entity) {
        if (entity == null) return;

    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organizationmember", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'OrganizationMember', 'DELETE')")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("OrganizationMember", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organizationmember", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'OrganizationMember', 'DELETE')")
    public Optional<OrganizationMember> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("OrganizationMember", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organizationmember", allEntries = true)
    @PreAuthorize("hasPermission(null, 'OrganizationMember', 'CREATE')")
    public OrganizationMember create(OrganizationMember t) { 
        OrganizationMember result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("OrganizationMember", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organizationmember", allEntries = true)
    @PreAuthorize("hasPermission(#t.id, 'OrganizationMember', 'UPDATE')")
    public OrganizationMember update(OrganizationMember t) { 
        if (t == null || t.getId() == null) return null;
        OrganizationMember existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        OrganizationMember result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("OrganizationMember", result));
        }
        return result;
    }

    private void mergeEntityData(OrganizationMember existing, OrganizationMember updated) {
        if (existing == null || updated == null) return;

        if (updated.getMemberRole() != null) {
            existing.setMemberRole(updated.getMemberRole());
        }
        if (updated.getJoinedAt() != null) {
            existing.setJoinedAt(updated.getJoinedAt());
        }
        if (updated.getIsActive() != null) {
            existing.setIsActive(updated.getIsActive());
        }
        if (updated.getInvitedByEmail() != null) {
            existing.setInvitedByEmail(updated.getInvitedByEmail().isEmpty() ? null : updated.getInvitedByEmail());
        }

        // Handle relationships
        if (updated.getOrganization() != null) {
            existing.setOrganization(updated.getOrganization());
        }
        if (updated.getUser() != null) {
            existing.setUser(updated.getUser());
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<OrganizationMember> update(List<OrganizationMember> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<OrganizationMember> result = new ArrayList<>();
        for (OrganizationMember entity : ts) {
            if (entity.getId() != null) {
                OrganizationMember updated = update(entity);
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
    public OrganizationMember findOrSave(OrganizationMember t) { 
        if (t == null) return null;
        
        OrganizationMember existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationMember findByReferenceEntity(OrganizationMember t) { 
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
    public OrganizationMember findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationMember> findByCriteria(OrganizationMemberCriteria criteria) {
        Specification<OrganizationMember> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new OrganizationMemberSpecification(criteria),
                buildOwnershipSpec("OrganizationMember")
        );
        return dao.findAll(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrganizationMember> findPaginatedByCriteria(OrganizationMemberCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<OrganizationMember> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new OrganizationMemberSpecification(criteria),
                buildOwnershipSpec("OrganizationMember")
        );
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(OrganizationMemberCriteria criteria) {
        Specification<OrganizationMember> spec = combineSpecs(
                (criteria == null || criteria.isEmpty()) ? null : new OrganizationMemberSpecification(criteria),
                buildOwnershipSpec("OrganizationMember")
        );
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<OrganizationMember> delete(List<OrganizationMember> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<OrganizationMember> deleted = new ArrayList<>();
        for (OrganizationMember entity : ts) {
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
    public OrganizationMember findByRef(String ref) { 
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
    private Specification<OrganizationMember> buildOwnershipSpec(String entityName) {
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
        Specification<OrganizationMember> combined = null;

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
            Specification<OrganizationMember> ownSpec = (root, query, cb) ->
                cb.equal(root.get("createdBy"), user.getId());
            combined = (combined == null) ? ownSpec : combined.or(ownSpec);
        }

        return combined;
    }

    /**
     * Combines two Specifications with AND logic, handling null gracefully.
     */
    private Specification<OrganizationMember> combineSpecs(Specification<OrganizationMember> criteriaSpec,
                                                       Specification<OrganizationMember> ownershipSpec) {
        if (criteriaSpec == null && ownershipSpec == null) return null;
        if (criteriaSpec == null) return ownershipSpec;
        if (ownershipSpec == null) return criteriaSpec;
        return criteriaSpec.and(ownershipSpec);
    }
}

