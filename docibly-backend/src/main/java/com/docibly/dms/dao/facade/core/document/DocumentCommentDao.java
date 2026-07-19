package com.docibly.dms.dao.facade.core.document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.docibly.dms.bean.core.document.DocumentComment;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentCommentDao extends JpaRepository<DocumentComment, Long>, JpaSpecificationExecutor<DocumentComment> {

    DocumentComment findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"document", "parentComment", "replies", "author"})
    @Override
    Page<DocumentComment> findAll(@Nullable Specification<DocumentComment> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"document", "parentComment", "replies", "author"})
    @Query("SELECT e FROM DocumentComment e WHERE e.id = :id")
    Optional<DocumentComment> findWithAssociationsById(@Param("id") Long id);

    List<DocumentComment> findByDocument_IdAndParentCommentIsNullOrderByCreatedDateAsc(Long documentId);

    List<DocumentComment> findByDocument_IdOrderByCreatedDateAsc(Long documentId);
}

