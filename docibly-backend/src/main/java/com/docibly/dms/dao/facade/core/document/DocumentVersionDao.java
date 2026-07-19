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
import com.docibly.dms.bean.core.document.DocumentVersion;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentVersionDao extends JpaRepository<DocumentVersion, Long>, JpaSpecificationExecutor<DocumentVersion> {

    DocumentVersion findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"document", "uploadedBy"})
    @Override
    Page<DocumentVersion> findAll(@Nullable Specification<DocumentVersion> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"document", "uploadedBy"})
    @Query("SELECT e FROM DocumentVersion e WHERE e.id = :id")
    Optional<DocumentVersion> findWithAssociationsById(@Param("id") Long id);

    List<DocumentVersion> findByDocument_IdOrderByVersionNumberDesc(Long documentId);

    Optional<DocumentVersion> findByDocument_IdAndIsCurrentVersionTrue(Long documentId);
}

