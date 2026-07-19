package com.docibly.dms.dao.facade.core.folder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.docibly.dms.bean.core.folder.Folder;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FolderDao extends JpaRepository<Folder, Long>, JpaSpecificationExecutor<Folder> {

    Folder findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"parentFolder", "subFolders", "organization", "documents"})
    @Override
    Page<Folder> findAll(@Nullable Specification<Folder> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"parentFolder", "subFolders", "organization", "documents"})
    @Query("SELECT e FROM Folder e WHERE e.id = :id")
    Optional<Folder> findWithAssociationsById(@Param("id") Long id);
}

