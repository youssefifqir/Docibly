package com.docibly.dms.service.facade.organization;

import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.dao.criteria.core.organization.OrganizationCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface OrganizationService {

    Organization create(Organization t);
    Organization update(Organization t);
    List<Organization> update(List<Organization> ts, boolean createIfNotExist);
    Optional<Organization> findById(Long id);
    Organization save(Organization entity);
    void deleteById(Long id);
    Optional<Organization> findAndDeleteById(Long id);
    Organization findOrSave(Organization t);
    Organization findByReferenceEntity(Organization t);
    Organization findWithAssociatedLists(Long id);
    List<Organization> findAll();
    List<Organization> findByCriteria(OrganizationCriteria criteria);
    Page<Organization> findPaginatedByCriteria(OrganizationCriteria criteria, Pageable pageable);
    int getDataSize(OrganizationCriteria criteria);
    List<Organization> delete(List<Organization> ts);
    Organization findByRef(String ref);
}

