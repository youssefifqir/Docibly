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
import com.docibly.dms.bean.core.document.Document;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentDao extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {

    Document findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"folder", "organization", "versions", "shares", "comments", "tags"})
    @Override
    Page<Document> findAll(@Nullable Specification<Document> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"folder", "organization", "versions", "shares", "comments", "tags"})
    @Query("SELECT e FROM Document e WHERE e.id = :id")
    Optional<Document> findWithAssociationsById(@Param("id") Long id);
}

