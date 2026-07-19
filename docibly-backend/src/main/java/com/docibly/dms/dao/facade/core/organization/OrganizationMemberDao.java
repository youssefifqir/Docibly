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
import com.docibly.dms.bean.core.organization.OrganizationMember;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationMemberDao extends JpaRepository<OrganizationMember, Long>, JpaSpecificationExecutor<OrganizationMember> {

    OrganizationMember findByRef(String ref);
    int deleteByRef(String ref);

    Optional<OrganizationMember> findByOrganization_IdAndUser_Id(Long organizationId, String userId);

    Optional<OrganizationMember> findByUser_Id(String userId);

    List<OrganizationMember> findAllByUser_Id(String userId);

    @EntityGraph(attributePaths = {"user"})
    List<OrganizationMember> findByOrganization_Id(Long organizationId);

    @EntityGraph(attributePaths = {"organization", "user"})
    @Override
    Page<OrganizationMember> findAll(@Nullable Specification<OrganizationMember> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"organization", "user"})
    @Query("SELECT e FROM OrganizationMember e WHERE e.id = :id")
    Optional<OrganizationMember> findWithAssociationsById(@Param("id") Long id);
}

