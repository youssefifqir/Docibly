package com.docibly.dms.service.impl.searchindex;

import com.docibly.dms.bean.core.searchindex.SearchIndex;
import com.docibly.dms.dao.criteria.core.searchindex.SearchIndexCriteria;
import com.docibly.dms.dao.facade.core.searchindex.SearchIndexDao;
import com.docibly.dms.dao.specification.core.searchindex.SearchIndexSpecification;
import com.docibly.dms.service.facade.searchindex.SearchIndexService;
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
public class SearchIndexServiceImpl implements SearchIndexService {

    private final SearchIndexDao dao;
    private final ApplicationEventPublisher eventPublisher;

    public SearchIndexServiceImpl(SearchIndexDao dao, ApplicationEventPublisher eventPublisher) {
        this.dao = dao;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_searchindex", key = "'list'")
    @PreAuthorize("hasPermission(null, 'SearchIndex', 'READ')")
    public List<SearchIndex> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "entity_searchindex", key = "#id")
    @PreAuthorize("hasPermission(#id, 'SearchIndex', 'READ')")
    public Optional<SearchIndex> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_searchindex", allEntries = true)
    public SearchIndex save(SearchIndex entity) {
        if (entity == null) return null;

        // Validate and handle relationships before saving
        validateAndPrepareRelationships(entity);

        return dao.save(entity);
    }

    private void validateAndPrepareRelationships(SearchIndex entity) {
        if (entity == null) return;

    }

    private void validateDeletionAllowed(SearchIndex entity) {
        if (entity == null) return;

    }

    private void prepareForDeletion(SearchIndex entity) {
        if (entity == null) return;

    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_searchindex", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'SearchIndex', 'DELETE')")
    public void deleteById(Long id) {
        if (id == null) return;

        findById(id).ifPresent(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("SearchIndex", entity));
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_searchindex", allEntries = true)
    @PreAuthorize("hasPermission(#id, 'SearchIndex', 'DELETE')")
    public Optional<SearchIndex> findAndDeleteById(Long id) {
        if (id == null) return Optional.empty();
        return findById(id).map(entity -> {
            validateDeletionAllowed(entity);
            prepareForDeletion(entity);
            entity.setDeletedAt(LocalDateTime.now());
            dao.save(entity);
            eventPublisher.publishEvent(EntityEvent.deleted("SearchIndex", entity));
            return entity;
        });
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_searchindex", allEntries = true)
    @PreAuthorize("hasPermission(null, 'SearchIndex', 'CREATE')")
    public SearchIndex create(SearchIndex t) { 
        SearchIndex result = save(t);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.created("SearchIndex", result));
        }
        return result;
    }

    @Override
    @Transactional(timeout = 30)
    @CacheEvict(value = "entity_searchindex", allEntries = true)
    @PreAuthorize("hasPermission(#t.id, 'SearchIndex', 'UPDATE')")
    public SearchIndex update(SearchIndex t) { 
        if (t == null || t.getId() == null) return null;
        SearchIndex existing = findById(t.getId()).orElse(null);
        if (existing == null) return null;

        validateAndPrepareRelationships(t);
        mergeEntityData(existing, t);

        SearchIndex result = save(existing);
        if (result != null) {
            eventPublisher.publishEvent(EntityEvent.updated("SearchIndex", result));
        }
        return result;
    }

    private void mergeEntityData(SearchIndex existing, SearchIndex updated) {
        if (existing == null || updated == null) return;

        if (updated.getDocumentId() != null) {
            existing.setDocumentId(updated.getDocumentId());
        }
        if (updated.getDocumentTitle() != null) {
            existing.setDocumentTitle(updated.getDocumentTitle().isEmpty() ? null : updated.getDocumentTitle());
        }
        if (updated.getFullText() != null) {
            existing.setFullText(updated.getFullText().isEmpty() ? null : updated.getFullText());
        }
        if (updated.getOcrText() != null) {
            existing.setOcrText(updated.getOcrText().isEmpty() ? null : updated.getOcrText());
        }
        if (updated.getTags() != null) {
            existing.setTags(updated.getTags().isEmpty() ? null : updated.getTags());
        }
        if (updated.getMimeType() != null) {
            existing.setMimeType(updated.getMimeType().isEmpty() ? null : updated.getMimeType());
        }
        if (updated.getOrganizationId() != null) {
            existing.setOrganizationId(updated.getOrganizationId());
        }
        if (updated.getOwnerId() != null) {
            existing.setOwnerId(updated.getOwnerId().isEmpty() ? null : updated.getOwnerId());
        }
        if (updated.getVisibility() != null) {
            existing.setVisibility(updated.getVisibility());
        }
        if (updated.getIndexedAt() != null) {
            existing.setIndexedAt(updated.getIndexedAt());
        }

        // Handle relationships
    }

    @Override
    @Transactional(timeout = 30)
    public List<SearchIndex> update(List<SearchIndex> ts, boolean createIfNotExist) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<SearchIndex> result = new ArrayList<>();
        for (SearchIndex entity : ts) {
            if (entity.getId() != null) {
                SearchIndex updated = update(entity);
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
    public SearchIndex findOrSave(SearchIndex t) { 
        if (t == null) return null;
        
        SearchIndex existing = null;
        if (t.getId() != null) {
            existing = findById(t.getId()).orElse(null);
        } else if (t.getRef() != null) {
            existing = findByRef(t.getRef());
        }
        
        return existing != null ? existing : save(t);
    }

    @Override
    @Transactional(readOnly = true)
    public SearchIndex findByReferenceEntity(SearchIndex t) { 
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
    public SearchIndex findWithAssociatedLists(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchIndex> findByCriteria(SearchIndexCriteria criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return findAll();
        }
        return dao.findAll(new SearchIndexSpecification(criteria));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SearchIndex> findPaginatedByCriteria(SearchIndexCriteria criteria, Pageable pageable) {
        if (pageable == null) {
            return Page.empty();
        }
        Specification<SearchIndex> spec = (criteria == null || criteria.isEmpty()) ? null : new SearchIndexSpecification(criteria);
        return dao.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public int getDataSize(SearchIndexCriteria criteria) {
        Specification<SearchIndex> spec = (criteria == null || criteria.isEmpty()) ? null : new SearchIndexSpecification(criteria);
        return (int) dao.count(spec);
    }

    @Override
    @Transactional(timeout = 30)
    public List<SearchIndex> delete(List<SearchIndex> ts) { 
        if (ts == null || ts.isEmpty()) return new ArrayList<>();
        
        List<SearchIndex> deleted = new ArrayList<>();
        for (SearchIndex entity : ts) {
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
    public SearchIndex findByRef(String ref) { 
        if (ref == null || ref.trim().isEmpty()) return null;
        return dao.findByRef(ref); 
    }
}

