package com.docibly.dms.dao.facade.core.organization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.docibly.dms.bean.core.organization.Organization;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationDao extends JpaRepository<Organization, Long>, JpaSpecificationExecutor<Organization> {

    Organization findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"organizationmembers", "organizationinvitations", "folders", "documents", "tags"})
    @Override
    Page<Organization> findAll(@Nullable Specification<Organization> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"organizationmembers", "organizationinvitations", "folders", "documents", "tags"})
    @Query("SELECT e FROM Organization e WHERE e.id = :id")
    Optional<Organization> findWithAssociationsById(@Param("id") Long id);

    Optional<Organization> findBySlug(String slug);
}

