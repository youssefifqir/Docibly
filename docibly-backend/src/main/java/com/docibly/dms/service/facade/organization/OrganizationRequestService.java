package com.docibly.dms.service.facade.organization;

import com.docibly.dms.bean.core.organization.OrganizationRequest;
import com.docibly.dms.dao.criteria.core.organization.OrganizationRequestCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface OrganizationRequestService {

    OrganizationRequest create(OrganizationRequest t);
    OrganizationRequest update(OrganizationRequest t);
    List<OrganizationRequest> update(List<OrganizationRequest> ts, boolean createIfNotExist);
    Optional<OrganizationRequest> findById(Long id);
    OrganizationRequest save(OrganizationRequest entity);
    void deleteById(Long id);
    Optional<OrganizationRequest> findAndDeleteById(Long id);
    OrganizationRequest findOrSave(OrganizationRequest t);
    OrganizationRequest findByReferenceEntity(OrganizationRequest t);
    OrganizationRequest findWithAssociatedLists(Long id);
    List<OrganizationRequest> findAll();
    List<OrganizationRequest> findByCriteria(OrganizationRequestCriteria criteria);
    Page<OrganizationRequest> findPaginatedByCriteria(OrganizationRequestCriteria criteria, Pageable pageable);
    int getDataSize(OrganizationRequestCriteria criteria);
    List<OrganizationRequest> delete(List<OrganizationRequest> ts);
    OrganizationRequest findByRef(String ref);
}

