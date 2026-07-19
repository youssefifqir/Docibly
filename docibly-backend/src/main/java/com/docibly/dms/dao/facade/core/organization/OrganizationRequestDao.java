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
import com.docibly.dms.bean.core.organization.OrganizationRequest;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRequestDao extends JpaRepository<OrganizationRequest, Long>, JpaSpecificationExecutor<OrganizationRequest> {

    OrganizationRequest findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"requestedBy", "reviewedBy"})
    @Override
    Page<OrganizationRequest> findAll(@Nullable Specification<OrganizationRequest> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"requestedBy", "reviewedBy"})
    @Query("SELECT e FROM OrganizationRequest e WHERE e.id = :id")
    Optional<OrganizationRequest> findWithAssociationsById(@Param("id") Long id);
}

