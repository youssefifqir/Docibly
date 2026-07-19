package com.docibly.dms.service.facade.organization;

import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.dao.criteria.core.organization.OrganizationInvitationCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface OrganizationInvitationService {

    OrganizationInvitation create(OrganizationInvitation t);
    OrganizationInvitation update(OrganizationInvitation t);
    List<OrganizationInvitation> update(List<OrganizationInvitation> ts, boolean createIfNotExist);
    Optional<OrganizationInvitation> findById(Long id);
    OrganizationInvitation save(OrganizationInvitation entity);
    void deleteById(Long id);
    Optional<OrganizationInvitation> findAndDeleteById(Long id);
    OrganizationInvitation findOrSave(OrganizationInvitation t);
    OrganizationInvitation findByReferenceEntity(OrganizationInvitation t);
    OrganizationInvitation findWithAssociatedLists(Long id);
    List<OrganizationInvitation> findAll();
    List<OrganizationInvitation> findByCriteria(OrganizationInvitationCriteria criteria);
    Page<OrganizationInvitation> findPaginatedByCriteria(OrganizationInvitationCriteria criteria, Pageable pageable);
    int getDataSize(OrganizationInvitationCriteria criteria);
    List<OrganizationInvitation> delete(List<OrganizationInvitation> ts);
    OrganizationInvitation findByRef(String ref);
}

