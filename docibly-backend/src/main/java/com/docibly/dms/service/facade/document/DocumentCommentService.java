package com.docibly.dms.service.facade.document;

import com.docibly.dms.bean.core.document.DocumentComment;
import com.docibly.dms.dao.criteria.core.document.DocumentCommentCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface DocumentCommentService {

    DocumentComment create(DocumentComment t);
    DocumentComment update(DocumentComment t);
    List<DocumentComment> update(List<DocumentComment> ts, boolean createIfNotExist);
    Optional<DocumentComment> findById(Long id);
    DocumentComment save(DocumentComment entity);
    void deleteById(Long id);
    Optional<DocumentComment> findAndDeleteById(Long id);
    DocumentComment findOrSave(DocumentComment t);
    DocumentComment findByReferenceEntity(DocumentComment t);
    DocumentComment findWithAssociatedLists(Long id);
    List<DocumentComment> findAll();
    List<DocumentComment> findByCriteria(DocumentCommentCriteria criteria);
    Page<DocumentComment> findPaginatedByCriteria(DocumentCommentCriteria criteria, Pageable pageable);
    int getDataSize(DocumentCommentCriteria criteria);
    List<DocumentComment> delete(List<DocumentComment> ts);
    DocumentComment findByRef(String ref);

    DocumentComment addComment(Long documentId, Long orgId, String content, Long parentCommentId);

    List<DocumentComment> getComments(Long documentId, Long orgId);

    DocumentComment resolveComment(Long commentId, Long orgId);

    DocumentComment editComment(Long commentId, Long orgId, String content);
}

