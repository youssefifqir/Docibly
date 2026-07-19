package com.docibly.dms.dao.facade.core.tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.docibly.dms.bean.core.tag.Tag;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TagDao extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    Tag findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"organization", "documents"})
    @Override
    Page<Tag> findAll(@Nullable Specification<Tag> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"organization", "documents"})
    @Query("SELECT e FROM Tag e WHERE e.id = :id")
    Optional<Tag> findWithAssociationsById(@Param("id") Long id);
}

