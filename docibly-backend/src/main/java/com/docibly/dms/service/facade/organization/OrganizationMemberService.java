package com.docibly.dms.service.facade.organization;

import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.dao.criteria.core.organization.OrganizationMemberCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface OrganizationMemberService {

    OrganizationMember create(OrganizationMember t);
    OrganizationMember update(OrganizationMember t);
    List<OrganizationMember> update(List<OrganizationMember> ts, boolean createIfNotExist);
    Optional<OrganizationMember> findById(Long id);
    OrganizationMember save(OrganizationMember entity);
    void deleteById(Long id);
    Optional<OrganizationMember> findAndDeleteById(Long id);
    OrganizationMember findOrSave(OrganizationMember t);
    OrganizationMember findByReferenceEntity(OrganizationMember t);
    OrganizationMember findWithAssociatedLists(Long id);
    List<OrganizationMember> findAll();
    List<OrganizationMember> findByCriteria(OrganizationMemberCriteria criteria);
    Page<OrganizationMember> findPaginatedByCriteria(OrganizationMemberCriteria criteria, Pageable pageable);
    int getDataSize(OrganizationMemberCriteria criteria);
    List<OrganizationMember> delete(List<OrganizationMember> ts);
    OrganizationMember findByRef(String ref);
}

