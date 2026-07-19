package com.docibly.dms.service.impl.tag;

import com.docibly.dms.bean.core.tag.Tag;
import com.docibly.dms.dao.criteria.core.tag.TagCriteria;
import com.docibly.dms.dao.facade.core.tag.TagDao;
import com.docibly.dms.dao.specification.core.tag.TagSpecification;
import com.docibly.dms.service.facade.tag.TagService;
import com.docibly.dms.service.facade.organization.OrganizationService;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.service.facade.document.DocumentService;
import com.docibly.dms.bean.core.document.Document;
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
public class TagServiceImpl implements TagService {

    private final TagDao dao;
    private final ApplicationEventPublisher eventPublisher;
    private final OrganizationService organizationService;
    private final DocumentService documentsService;

    public TagServiceImpl(TagDao dao, ApplicationEventPublisher eventPublisher, @Lazy OrganizationService organizationService, @Lazy DocumentService documentsService) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
        this.organizationService = organizationService;
        this.documentsService = documentsService;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_tag", key = "'list'")
    public List<Tag> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_tag", key = "#id")
    public Optional<Tag> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_tag", allEntries = true)
    public Tag save(Tag entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(Tag entity) {
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
        // Validate ManyToMany relationships exist
        if (entity.getDocuments() != null) {
            Set<Document> validatedDocuments = new HashSet<>();
            for (Document relatedEntity : entity.getDocuments()) {
                if (relatedEntity != null) {
                    if (relatedEntity.getId() != null) {
                        Document existing = documentsService.findById(relatedEntity.getId()).orElse(null);
                        if (existing == null) {
                            throw new IllegalArgumentException("Documents with id " + relatedEntity.getId() + " does not exist");
                        }
                        validatedDocuments.add(existing);
                    } else if (relatedEntity.getRef() != null) {
                        Document existing = documentsService.findByRef(relatedEntity.getRef());
                        if (existing == null) {
                            throw new IllegalArgumentException("Documents with ref '" + relatedEntity.getRef() + "' does not exist");
                        }
                        validatedDocuments.add(existing);
                    } else {
                        throw new IllegalArgumentException("Documents relationship must be referenced by id or ref");
                    }
                }
            }
            entity.setDocuments(validatedDocuments);
        }
    }

    private void validateDeletionAllowed(Tag entity) {
        if (entity == null) return;

    }

    private void prepareForDeletion(Tag entity) {
        if (entity == null) return;

        // Clear ManyToMany relationships to avoid constraint violations
        if (entity.getDocuments() != null) {
            entity.getDocuments().clear();
        }
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_tag", allEntries = true)
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Tag", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_tag", allEntries = true)
    public Optional<Tag> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("Tag", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_tag", allEntries = true)
    public Tag create(Tag t) {
        Tag result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("Tag", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_tag", allEntries = true)
    public Tag update(Tag t) {
        if (t == null || t.getId() == null) return null;
        Tag existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        Tag result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("Tag", result));
        }
        return result;
    }

    private void mergeEntityData(Tag existing, Tag updated) {
        if (existing == null || updated == null) return;

        if (updated.getName() != null) {
            existing.setName(updated.getName().isEmpty() ? null : updated.getName());
        }
        if (updated.getSlug() != null) {
            existing.setSlug(updated.getSlug().isEmpty() ? null : updated.getSlug());
        }
        if (updated.getColor() != null) {
            existing.setColor(updated.getColor().isEmpty() ? null : updated.getColor());
        }
        if (updated.getUsageCount() != null) {
            existing.setUsageCount(updated.getUsageCount());
        }

        // Handle relationships
        if (updated.getOrganization() != null) {
            existing.setOrganization(updated.getOrganization());
        }
        if (updated.getDocuments() != null) {
            existing.setDocuments(updated.getDocuments());
        }
    }

    @Override
    @Transactional(timeout = 30)
    public List<Tag> update(List<Tag> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Tag> result = new ArrayList<>();
        for (Tag entity : ts) {
            if (entity.getId() != null) {
                Tag updated = update(entity);
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
    public Tag findOrSave(Tag t) { 
        if (t == null) return null;
        
        Tag existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public Tag findByReferenceEntity(Tag t) { 
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
    public Tag findWithAssociatedLists(Long id) {
        return dao.findWithAssociationsById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findByCriteria(TagCriteria criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return findAll();
        }
        return dao.findAll(new TagSpecification(criteria));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tag> findPaginatedByCriteria(TagCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<Tag> spec = (criteria == null || criteria.isEmpty()) ? null : new TagSpecification(criteria);
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(TagCriteria criteria) {
        Specification<Tag> spec = (criteria == null || criteria.isEmpty()) ? null : new TagSpecification(criteria);
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<Tag> delete(List<Tag> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<Tag> deleted = new ArrayList<>();
        for (Tag entity : ts) {
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
    public Tag findByRef(String ref) { 
        if (ref == null || ref.trim().isEmpty()) return null;
        return dao.findByRef(ref); 
    }
}

