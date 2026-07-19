package com.docibly.dms.service.impl.document;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.dao.criteria.core.document.DocumentCriteria;
import com.docibly.dms.dao.facade.core.document.DocumentDao;
import com.docibly.dms.dao.specification.core.document.DocumentSpecification;
import com.docibly.dms.service.facade.document.DocumentService;
import com.docibly.dms.service.facade.folder.FolderService;
import com.docibly.dms.bean.core.folder.Folder;
import com.docibly.dms.service.facade.organization.OrganizationService;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.service.facade.tag.TagService;
import com.docibly.dms.bean.core.tag.Tag;
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
public class DocumentServiceImpl implements DocumentService {

    private final DocumentDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final FolderService folderService;
    private final OrganizationService organizationService;
    private final TagService tagsService;

    public DocumentServiceImpl(DocumentDao dao, ApplicationEventPublisher eventPublisher, @Lazy FolderService folderService, @Lazy OrganizationService organizationService, @Lazy TagService tagsService) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.folderService = folderService;
        this.organizationService = organizationService;
        this.tagsService = tagsService;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_document", key = "'list'")
    @PreAuthorize("hasPermission(null, 'Document', 'READ')")
    public List<Document> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_document", key = "#id")
    @PreAuthorize("hasPermission(#id, 'Document', 'READ')")
    public Optional<Document> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_document", allEntries = true)
    public Document save(Document entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(Document entity) {
        if (entity == null) return;

        // Validate ManyToOne relationships exist
        if (entity.getFolder() != null) {
            Folder folderEntity = entity.getFolder();
            if (folderEntity.getId() != null) {
                Folder existingFolder = folderService.findById(folderEntity.getId()).orElse(null);
                if (existingFolder == null) {
                    throw new IllegalArgumentException("Folder with id " + folderEntity.getId() + " does not exist");
                }
                entity.setFolder(existingFolder);
            } else if (folderEntity.getRef() != null) {
                Folder existingFolder = folderService.findByRef(folderEntity.getRef());
                if (existingFolder == null) {
                    throw new IllegalArgumentException("Folder with ref '" + folderEntity.getRef() + "' does not exist");
                }
                entity.setFolder(existingFolder);
            } else {
                throw new IllegalArgumentException("Folder must be referenced by id or ref");
            }
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
        if (entity.getVersions() != null) {
            entity.getVersions().forEach(child -> {
                if (child != null) {
                    child.setDocument(entity);
                }
            });
        }
        // Handle OneToMany relationships - set parent reference
        if (entity.getShares() != null) {
            entity.getShares().forEach(child -> {
                if (child != null) {
                    child.setDocument(entity);
                }
            });
        }
        // Handle OneToMany relationships - set parent reference
        if (entity.getComments() != null) {
            entity.getComments().forEach(child -> {
                if (child != null) {
                    child.setDocument(entity);
                }
            });
        }
        // Validate ManyToMany relationships exist
        if (entity.getTags() != null) {
            Set<Tag> validatedTags = new HashSet<>();
            for (Tag relatedEntity : entity.getTags()) {
                if (relatedEntity != null) {
                    if (relatedEntity.getId() != null) {
                        Tag existing = tagsService.findById(relatedEntity.getId()).orElse(null);
                        if (existing == null) {
                            throw new IllegalArgumentException("Tags with id " + relatedEntity.getId() + " does not exist");
                        }
                        validatedTags.add(existing);
                    } else if (relatedEntity.getRef() != null) {
                        Tag existing = tagsService.findByRef(relatedEntity.getRef());
                        if (existing == null) {
                            throw new IllegalArgumentException("Tags with ref '" + relatedEntity.getRef() + "' does not exist");
                        }
                        validatedTags.add(existing);
                    } else {
                        throw new IllegalArgumentException("Tags relationship must be referenced by id or ref");
                    }
                }
            }
            entity.setTags(validatedTags);
        }
    }

    private void validateDeletionAllowed(Document entity) {
        if (entity == null) return;

        // Keep as an extension point for domain-specific delete guards.
        // Keep as an extension point for domain-specific delete guards.
        // Keep as an extension point for domain-specific delete guards.
    }

    private void prepareForDeletion(Document entity) {
        if (entity == null) return;

        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
        // No-op for OneToMany: rely on JPA cascade/orphanRemoval configuration.
        // Clear ManyToMany relationships to avoid constraint violations
        if (entity.getTags() != null) {
            entity.getTags().clear();
        }
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_document", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'Document', 'DELETE')")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Document", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_document", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'Document', 'DELETE')")
    public Optional<Document> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Document", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_document", allEntries = true)
    @PreAuthorize("hasPermission(null, 'Document', 'CREATE')")
    public Document create(Document t) { 
        Document result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("Document", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_document", allEntries = true)
    @PreAuthorize("hasPermission(#t.id, 'Document', 'UPDATE')")
    public Document update(Document t) { 
        if (t == null || t.getId() == null) return null;
        Document existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        Document result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("Document", result));
        }
        return result;
    }

    private void mergeEntityData(Document existing, Document updated) {
        if (existing == null || updated == null) return;

        if (updated.getTitle() != null) {
            existing.setTitle(updated.getTitle().isEmpty() ? null : updated.getTitle());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription().isEmpty() ? null : updated.getDescription());
        }
        if (updated.getOriginalFilename() != null) {
            existing.setOriginalFilename(updated.getOriginalFilename().isEmpty() ? null : updated.getOriginalFilename());
        }
        if (updated.getStoredFilename() != null) {
            existing.setStoredFilename(updated.getStoredFilename().isEmpty() ? null : updated.getStoredFilename());
        }
        if (updated.getMimeType() != null) {
            existing.setMimeType(updated.getMimeType().isEmpty() ? null : updated.getMimeType());
        }
        if (updated.getFileSizeBytes() != null) {
            existing.setFileSizeBytes(updated.getFileSizeBytes());
        }
        if (updated.getStorageBucket() != null) {
            existing.setStorageBucket(updated.getStorageBucket().isEmpty() ? null : updated.getStorageBucket());
        }
        if (updated.getStorageKey() != null) {
            existing.setStorageKey(updated.getStorageKey().isEmpty() ? null : updated.getStorageKey());
        }
        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
        if (updated.getVisibility() != null) {
            existing.setVisibility(updated.getVisibility());
        }
        if (updated.getCurrentVersionNumber() != null) {
            existing.setCurrentVersionNumber(updated.getCurrentVersionNumber());
        }
        if (updated.getDownloadCount() != null) {
            existing.setDownloadCount(updated.getDownloadCount());
        }
        if (updated.getViewCount() != null) {
            existing.setViewCount(updated.getViewCount());
        }
        if (updated.getIsPasswordProtected() != null) {
            existing.setIsPasswordProtected(updated.getIsPasswordProtected());
        }
        if (updated.getPasswordHash() != null) {
            existing.setPasswordHash(updated.getPasswordHash().isEmpty() ? null : updated.getPasswordHash());
        }
        if (updated.getExpiresAt() != null) {
            existing.setExpiresAt(updated.getExpiresAt());
        }
        if (updated.getArchivedAt() != null) {
            existing.setArchivedAt(updated.getArchivedAt());
        }
        if (updated.getChecksum() != null) {
            existing.setChecksum(updated.getChecksum().isEmpty() ? null : updated.getChecksum());
        }
        if (updated.getOcrStatus() != null) {
            existing.setOcrStatus(updated.getOcrStatus());
        }
        if (updated.getOcrText() != null) {
            existing.setOcrText(updated.getOcrText().isEmpty() ? null : updated.getOcrText());
        }
        if (updated.getOcrProcessedAt() != null) {
            existing.setOcrProcessedAt(updated.getOcrProcessedAt());
        }
        if (updated.getOcrLanguage() != null) {
            existing.setOcrLanguage(updated.getOcrLanguage().isEmpty() ? null : updated.getOcrLanguage());
        }
        if (updated.getOcrConfidenceScore() != null) {
            existing.setOcrConfidenceScore(updated.getOcrConfidenceScore());
        }

        // Handle relationships
        if (updated.getFolder() != null) {
            existing.setFolder(updated.getFolder());
        }
        if (updated.getOrganization() != null) {
            existing.setOrganization(updated.getOrganization());
        }
        if (updated.getVersions() != null) {
            if (existing.getVersions() == null) {
                existing.setVersions(new LinkedHashSet<>());
            } else {
                existing.getVersions().clear();
            }

            updated.getVersions().forEach(child -> {
                if (child != null) {
                    child.setDocument(existing);
                    existing.getVersions().add(child);
                }
            });
        }
        if (updated.getShares() != null) {
            if (existing.getShares() == null) {
                existing.setShares(new LinkedHashSet<>());
            } else {
                existing.getShares().clear();
            }

            updated.getShares().forEach(child -> {
                if (child != null) {
                    child.setDocument(existing);
                    existing.getShares().add(child);
                }
            });
        }
        if (updated.getComments() != null) {
            if (existing.getComments() == null) {
                existing.setComments(new LinkedHashSet<>());
            } else {
                existing.getComments().clear();
            }

            updated.getComments().forEach(child -> {
                if (child != null) {
                    child.setDocument(existing);
                    existing.getComments().add(child);
                }
            });
        }
        if (updated.getTags() != null) {
            existing.setTags(updated.getTags());
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<Document> update(List<Document> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Document> result = new ArrayList<>();
        for (Document entity : ts) {
            if (entity.getId() != null) {
                Document updated = update(entity);
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
    public Document findOrSave(Document t) { 
        if (t == null) return null;
        
        Document existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Document findByReferenceEntity(Document t) { 
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
    public Document findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> findByCriteria(DocumentCriteria criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return findAll();
        }
        return dao.findAll(new DocumentSpecification(criteria));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Document> findPaginatedByCriteria(DocumentCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<Document> spec = (criteria == null || criteria.isEmpty()) ? null : new DocumentSpecification(criteria);
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(DocumentCriteria criteria) {
        Specification<Document> spec = (criteria == null || criteria.isEmpty()) ? null : new DocumentSpecification(criteria);
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<Document> delete(List<Document> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Document> deleted = new ArrayList<>();
        for (Document entity : ts) {
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
    public Document findByRef(String ref) { 
        if (ref == null || ref.trim().isEmpty()) return null;
        return dao.findByRef(ref); 
    }
}

