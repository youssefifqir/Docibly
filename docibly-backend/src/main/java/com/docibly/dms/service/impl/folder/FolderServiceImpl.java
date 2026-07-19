package com.docibly.dms.service.impl.folder;

import com.docibly.dms.bean.core.folder.Folder;
import com.docibly.dms.dao.criteria.core.folder.FolderCriteria;
import com.docibly.dms.dao.facade.core.folder.FolderDao;
import com.docibly.dms.dao.specification.core.folder.FolderSpecification;
import com.docibly.dms.service.facade.folder.FolderService;
import com.docibly.dms.service.facade.folder.FolderService;
import com.docibly.dms.bean.core.folder.Folder;
import com.docibly.dms.service.facade.organization.OrganizationService;
import com.docibly.dms.bean.core.organization.Organization;
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
public class FolderServiceImpl implements FolderService {

    private final FolderDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final FolderService parentFolderService;
    private final OrganizationService organizationService;

    public FolderServiceImpl(FolderDao dao, ApplicationEventPublisher eventPublisher, @Lazy FolderService parentFolderService, @Lazy OrganizationService organizationService) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.parentFolderService = parentFolderService;
        this.organizationService = organizationService;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_folder", key = "'list'")
    public List<Folder> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_folder", key = "#id")
    public Optional<Folder> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_folder", allEntries = true)
    public Folder save(Folder entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(Folder entity) {
        if (entity == null) return;

        // Validate ManyToOne relationships exist
        if (entity.getParentFolder() != null) {
            Folder parentFolderEntity = entity.getParentFolder();
            if (parentFolderEntity.getId() != null) {
                Folder existingParentFolder = parentFolderService.findById(parentFolderEntity.getId()).orElse(null);
                if (existingParentFolder == null) {
                    throw new IllegalArgumentException("ParentFolder with id " + parentFolderEntity.getId() + " does not exist");
                }
                entity.setParentFolder(existingParentFolder);
            } else if (parentFolderEntity.getRef() != null) {
                Folder existingParentFolder = parentFolderService.findByRef(parentFolderEntity.getRef());
                if (existingParentFolder == null) {
                    throw new IllegalArgumentException("ParentFolder with ref '" + parentFolderEntity.getRef() + "' does not exist");
                }
                entity.setParentFolder(existingParentFolder);
            } else {
                throw new IllegalArgumentException("ParentFolder must be referenced by id or ref");
            }
        }
        // Handle OneToMany relationships - set parent reference
        if (entity.getSubFolders() != null) {
            entity.getSubFolders().forEach(child -> {
                if (child != null) {
                    child.setParentFolder(entity);
                }
            });
        }
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
        // Handle OneToMany relationships - set parent reference
        if (entity.getDocuments() != null) {
            entity.getDocuments().forEach(child -> {
                if (child != null) {
                    child.setFolder(entity);
                }
            });
        }
    }

    private void validateDeletionAllowed(Folder entity) {
        if (entity == null) return;

        // Keep as an extension point for domain-specific delete guards.
        // Keep as an extension point for domain-specific delete guards.
    }

    private void prepareForDeletion(Folder entity) {
        if (entity == null) return;

        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_folder", allEntries = true)
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Folder", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_folder", allEntries = true)
    public Optional<Folder> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Folder", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_folder", allEntries = true)
    public Folder create(Folder t) {
        Folder result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("Folder", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_folder", allEntries = true)
    public Folder update(Folder t) {
        if (t == null || t.getId() == null) return null;
        Folder existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        Folder result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("Folder", result));
        }
        return result;
    }

    private void mergeEntityData(Folder existing, Folder updated) {
        if (existing == null || updated == null) return;

        if (updated.getName() != null) {
            existing.setName(updated.getName().isEmpty() ? null : updated.getName());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription().isEmpty() ? null : updated.getDescription());
        }
        if (updated.getColor() != null) {
            existing.setColor(updated.getColor().isEmpty() ? null : updated.getColor());
        }
        if (updated.getIconName() != null) {
            existing.setIconName(updated.getIconName().isEmpty() ? null : updated.getIconName());
        }
        if (updated.getIsShared() != null) {
            existing.setIsShared(updated.getIsShared());
        }
        if (updated.getDocumentCount() != null) {
            existing.setDocumentCount(updated.getDocumentCount());
        }
        if (updated.getTotalSizeBytes() != null) {
            existing.setTotalSizeBytes(updated.getTotalSizeBytes());
        }

        // Handle relationships
        if (updated.getParentFolder() != null) {
            existing.setParentFolder(updated.getParentFolder());
        }
        if (updated.getSubFolders() != null) {
            if (existing.getSubFolders() == null) {
                existing.setSubFolders(new LinkedHashSet<>());
            } else {
                existing.getSubFolders().clear();
            }

            updated.getSubFolders().forEach(child -> {
                if (child != null) {
                    child.setParentFolder(existing);
                    existing.getSubFolders().add(child);
                }
            });
        }
        if (updated.getOrganization() != null) {
            existing.setOrganization(updated.getOrganization());
        }
        if (updated.getDocuments() != null) {
            if (existing.getDocuments() == null) {
                existing.setDocuments(new LinkedHashSet<>());
            } else {
                existing.getDocuments().clear();
            }

            updated.getDocuments().forEach(child -> {
                if (child != null) {
                    child.setFolder(existing);
                    existing.getDocuments().add(child);
                }
            });
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<Folder> update(List<Folder> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Folder> result = new ArrayList<>();
        for (Folder entity : ts) {
            if (entity.getId() != null) {
                Folder updated = update(entity);
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
    public Folder findOrSave(Folder t) { 
        if (t == null) return null;
        
        Folder existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Folder findByReferenceEntity(Folder t) { 
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
    public Folder findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Folder> findByCriteria(FolderCriteria criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return findAll();
        }
        return dao.findAll(new FolderSpecification(criteria));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Folder> findPaginatedByCriteria(FolderCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<Folder> spec = (criteria == null || criteria.isEmpty()) ? null : new FolderSpecification(criteria);
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(FolderCriteria criteria) {
        Specification<Folder> spec = (criteria == null || criteria.isEmpty()) ? null : new FolderSpecification(criteria);
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<Folder> delete(List<Folder> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Folder> deleted = new ArrayList<>();
        for (Folder entity : ts) {
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
    public Folder findByRef(String ref) { 
        if (ref == null || ref.trim().isEmpty()) return null;
        return dao.findByRef(ref); 
    }
}

