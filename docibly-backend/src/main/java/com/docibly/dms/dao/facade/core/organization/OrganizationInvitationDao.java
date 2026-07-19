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
import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationInvitationDao extends JpaRepository<OrganizationInvitation, Long>, JpaSpecificationExecutor<OrganizationInvitation> {

    OrganizationInvitation findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"organization", "invitedBy"})
    @Override
    Page<OrganizationInvitation> findAll(@Nullable Specification<OrganizationInvitation> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"organization", "invitedBy"})
    @Query("SELECT e FROM OrganizationInvitation e WHERE e.id = :id")
    Optional<OrganizationInvitation> findWithAssociationsById(@Param("id") Long id);

    Optional<OrganizationInvitation> findByToken(String token);
}

