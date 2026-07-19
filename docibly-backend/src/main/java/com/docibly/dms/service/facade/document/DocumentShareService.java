package com.docibly.dms.service.facade.document;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.document.DocumentShare;
import com.docibly.dms.bean.core.enums.SharePermission;
import com.docibly.dms.dao.criteria.core.document.DocumentShareCriteria;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Validated
public interface DocumentShareService {

    DocumentShare create(DocumentShare t);
    DocumentShare update(DocumentShare t);
    List<DocumentShare> update(List<DocumentShare> ts, boolean createIfNotExist);
    Optional<DocumentShare> findById(Long id);
    DocumentShare save(DocumentShare entity);
    void deleteById(Long id);
    Optional<DocumentShare> findAndDeleteById(Long id);
    DocumentShare findOrSave(DocumentShare t);
    DocumentShare findByReferenceEntity(DocumentShare t);
    DocumentShare findWithAssociatedLists(Long id);
    List<DocumentShare> findAll();
    List<DocumentShare> findByCriteria(DocumentShareCriteria criteria);
    Page<DocumentShare> findPaginatedByCriteria(DocumentShareCriteria criteria, Pageable pageable);
    int getDataSize(DocumentShareCriteria criteria);
    List<DocumentShare> delete(List<DocumentShare> ts);
    DocumentShare findByRef(String ref);

    DocumentShare createShare(Long documentId, Long orgId, SharePermission permission,
                              LocalDateTime expiresAt, String password);

    DocumentShare findByToken(String token);

    DocumentShare verifyPassword(String token, String password);

    DocumentInfo accessShare(String token, String password);

    void revokeShare(Long shareId, Long orgId);

    record DocumentInfo(DocumentShare share, Document document, Resource resource) {}
}
