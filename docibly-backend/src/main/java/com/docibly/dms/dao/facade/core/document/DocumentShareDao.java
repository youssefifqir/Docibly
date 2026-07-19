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
import com.docibly.dms.bean.core.document.DocumentShare;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentShareDao extends JpaRepository<DocumentShare, Long>, JpaSpecificationExecutor<DocumentShare> {

    DocumentShare findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"document", "sharedBy", "sharedWith"})
    @Override
    Page<DocumentShare> findAll(@Nullable Specification<DocumentShare> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"document", "sharedBy", "sharedWith"})
    @Query("SELECT e FROM DocumentShare e WHERE e.id = :id")
    Optional<DocumentShare> findWithAssociationsById(@Param("id") Long id);

    Optional<DocumentShare> findByShareToken(String shareToken);
}

