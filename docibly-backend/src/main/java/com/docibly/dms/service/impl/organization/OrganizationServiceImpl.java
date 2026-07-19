package com.docibly.dms.service.impl.organization;

import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.dao.criteria.core.organization.OrganizationCriteria;
import com.docibly.dms.dao.facade.core.organization.OrganizationDao;
import com.docibly.dms.dao.specification.core.organization.OrganizationSpecification;
import com.docibly.dms.service.facade.organization.OrganizationService;
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
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationDao dao;
    private final ApplicationEventPublisher eventPublisher;

    public OrganizationServiceImpl(OrganizationDao dao, ApplicationEventPublisher eventPublisher) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_organization", key = "'list'")
    @PreAuthorize("hasPermission(null, 'Organization', 'READ')")
    public List<Organization> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_organization", key = "#id")
    @PreAuthorize("hasPermission(#id, 'Organization', 'READ')")
    public Optional<Organization> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organization", allEntries = true)
    public Organization save(Organization entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(Organization entity) {
        if (entity == null) return;

        // Handle OneToMany relationships - set parent reference
        if (entity.getOrganizationmembers() != null) {
            entity.getOrganizationmembers().forEach(child -> {
                if (child != null) {
                    child.setOrganization(entity);
                }
            });
        }
        // Handle OneToMany relationships - set parent reference
        if (entity.getOrganizationinvitations() != null) {
            entity.getOrganizationinvitations().forEach(child -> {
                if (child != null) {
                    child.setOrganization(entity);
                }
            });
        }
        // Handle OneToMany relationships - set parent reference
        if (entity.getFolders() != null) {
            entity.getFolders().forEach(child -> {
                if (child != null) {
                    child.setOrganization(entity);
                }
            });
        }
        // Handle OneToMany relationships - set parent reference
        if (entity.getDocuments() != null) {
            entity.getDocuments().forEach(child -> {
                if (child != null) {
                    child.setOrganization(entity);
                }
            });
        }
        // Handle OneToMany relationships - set parent reference
        if (entity.getTags() != null) {
            entity.getTags().forEach(child -> {
                if (child != null) {
                    child.setOrganization(entity);
                }
            });
        }
    }

    private void validateDeletionAllowed(Organization entity) {
        if (entity == null) return;

        // Keep as an extension point for domain-specific delete guards.
        // Keep as an extension point for domain-specific delete guards.
        // Keep as an extension point for domain-specific delete guards.
        // Keep as an extension point for domain-specific delete guards.
        // Keep as an extension point for domain-specific delete guards.
    }

    private void prepareForDeletion(Organization entity) {
        if (entity == null) return;

        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organization", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'Organization', 'DELETE')")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Organization", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organization", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'Organization', 'DELETE')")
    public Optional<Organization> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Organization", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organization", allEntries = true)
    @PreAuthorize("hasPermission(null, 'Organization', 'CREATE')")
    public Organization create(Organization t) { 
        Organization result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("Organization", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_organization", allEntries = true)
    @PreAuthorize("hasPermission(#t.id, 'Organization', 'UPDATE')")
    public Organization update(Organization t) { 
        if (t == null || t.getId() == null) return null;
        Organization existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        Organization result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("Organization", result));
        }
        return result;
    }

    private void mergeEntityData(Organization existing, Organization updated) {
        if (existing == null || updated == null) return;

        if (updated.getName() != null) {
            existing.setName(updated.getName().isEmpty() ? null : updated.getName());
        }
        if (updated.getSlug() != null) {
            existing.setSlug(updated.getSlug().isEmpty() ? null : updated.getSlug());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription().isEmpty() ? null : updated.getDescription());
        }
        if (updated.getLogoUrl() != null) {
            existing.setLogoUrl(updated.getLogoUrl().isEmpty() ? null : updated.getLogoUrl());
        }
        if (updated.getWebsite() != null) {
            existing.setWebsite(updated.getWebsite().isEmpty() ? null : updated.getWebsite());
        }
        if (updated.getStorageUsedBytes() != null) {
            existing.setStorageUsedBytes(updated.getStorageUsedBytes());
        }
        if (updated.getStorageQuotaBytes() != null) {
            existing.setStorageQuotaBytes(updated.getStorageQuotaBytes());
        }
        if (updated.getMaxMembers() != null) {
            existing.setMaxMembers(updated.getMaxMembers());
        }
        if (updated.getIsActive() != null) {
            existing.setIsActive(updated.getIsActive());
        }
        if (updated.getBillingEmail() != null) {
            existing.setBillingEmail(updated.getBillingEmail().isEmpty() ? null : updated.getBillingEmail());
        }
        if (updated.getPlanTier() != null) {
            existing.setPlanTier(updated.getPlanTier().isEmpty() ? null : updated.getPlanTier());
        }
        if (updated.getTrialEndsAt() != null) {
            existing.setTrialEndsAt(updated.getTrialEndsAt());
        }

        // Handle relationships
        if (updated.getOrganizationmembers() != null) {
            if (existing.getOrganizationmembers() == null) {
                existing.setOrganizationmembers(new LinkedHashSet<>());
            } else {
                existing.getOrganizationmembers().clear();
            }

            updated.getOrganizationmembers().forEach(child -> {
                if (child != null) {
                    child.setOrganization(existing);
                    existing.getOrganizationmembers().add(child);
                }
            });
        }
        if (updated.getOrganizationinvitations() != null) {
            if (existing.getOrganizationinvitations() == null) {
                existing.setOrganizationinvitations(new LinkedHashSet<>());
            } else {
                existing.getOrganizationinvitations().clear();
            }

            updated.getOrganizationinvitations().forEach(child -> {
                if (child != null) {
                    child.setOrganization(existing);
                    existing.getOrganizationinvitations().add(child);
                }
            });
        }
        if (updated.getFolders() != null) {
            if (existing.getFolders() == null) {
                existing.setFolders(new LinkedHashSet<>());
            } else {
                existing.getFolders().clear();
            }

            updated.getFolders().forEach(child -> {
                if (child != null) {
                    child.setOrganization(existing);
                    existing.getFolders().add(child);
                }
            });
        }
        if (updated.getDocuments() != null) {
            if (existing.getDocuments() == null) {
                existing.setDocuments(new LinkedHashSet<>());
            } else {
                existing.getDocuments().clear();
            }

            updated.getDocuments().forEach(child -> {
                if (child != null) {
                    child.setOrganization(existing);
                    existing.getDocuments().add(child);
                }
            });
        }
        if (updated.getTags() != null) {
            if (existing.getTags() == null) {
                existing.setTags(new LinkedHashSet<>());
            } else {
                existing.getTags().clear();
            }

            updated.getTags().forEach(child -> {
                if (child != null) {
                    child.setOrganization(existing);
                    existing.getTags().add(child);
                }
            });
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<Organization> update(List<Organization> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Organization> result = new ArrayList<>();
        for (Organization entity : ts) {
            if (entity.getId() != null) {
                Organization updated = update(entity);
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
    public Organization findOrSave(Organization t) { 
        if (t == null) return null;
        
        Organization existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Organization findByReferenceEntity(Organization t) { 
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
    public Organization findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Organization> findByCriteria(OrganizationCriteria criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return findAll();
        }
        return dao.findAll(new OrganizationSpecification(criteria));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Organization> findPaginatedByCriteria(OrganizationCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<Organization> spec = (criteria == null || criteria.isEmpty()) ? null : new OrganizationSpecification(criteria);
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(OrganizationCriteria criteria) {
        Specification<Organization> spec = (criteria == null || criteria.isEmpty()) ? null : new OrganizationSpecification(criteria);
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<Organization> delete(List<Organization> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Organization> deleted = new ArrayList<>();
        for (Organization entity : ts) {
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
    public Organization findByRef(String ref) { 
        if (ref == null || ref.trim().isEmpty()) return null;
        return dao.findByRef(ref); 
    }
}

