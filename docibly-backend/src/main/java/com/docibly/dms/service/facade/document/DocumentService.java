package com.docibly.dms.service.facade.document;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.dao.criteria.core.document.DocumentCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface DocumentService {

    Document create(Document t);
    Document update(Document t);
    List<Document> update(List<Document> ts, boolean createIfNotExist);
    Optional<Document> findById(Long id);
    Document save(Document entity);
    void deleteById(Long id);
    Optional<Document> findAndDeleteById(Long id);
    Document findOrSave(Document t);
    Document findByReferenceEntity(Document t);
    Document findWithAssociatedLists(Long id);
    List<Document> findAll();
    List<Document> findByCriteria(DocumentCriteria criteria);
    Page<Document> findPaginatedByCriteria(DocumentCriteria criteria, Pageable pageable);
    int getDataSize(DocumentCriteria criteria);
    List<Document> delete(List<Document> ts);
    Document findByRef(String ref);
}

