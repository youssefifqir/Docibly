package com.docibly.dms.service.facade.document;

import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.dao.criteria.core.document.DocumentVersionCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;

@Validated
public interface DocumentVersionService {

    DocumentVersion create(DocumentVersion t);
    DocumentVersion update(DocumentVersion t);
    List<DocumentVersion> update(List<DocumentVersion> ts, boolean createIfNotExist);
    Optional<DocumentVersion> findById(Long id);
    DocumentVersion save(DocumentVersion entity);
    void deleteById(Long id);
    Optional<DocumentVersion> findAndDeleteById(Long id);
    DocumentVersion findOrSave(DocumentVersion t);
    DocumentVersion findByReferenceEntity(DocumentVersion t);
    DocumentVersion findWithAssociatedLists(Long id);
    List<DocumentVersion> findAll();
    List<DocumentVersion> findByCriteria(DocumentVersionCriteria criteria);
    Page<DocumentVersion> findPaginatedByCriteria(DocumentVersionCriteria criteria, Pageable pageable);
    int getDataSize(DocumentVersionCriteria criteria);
    List<DocumentVersion> delete(List<DocumentVersion> ts);
    DocumentVersion findByRef(String ref);

    DocumentVersion reupload(Long documentId, Long orgId, MultipartFile file,
                               String label, String changeNote);

    List<DocumentVersion> findByDocumentId(Long documentId);
}

