package com.docibly.dms.service.facade.department;

import com.docibly.dms.bean.core.department.DepartmentRoleDefinition;

import java.util.List;
import java.util.Optional;

public interface DepartmentRoleService {

    List<DepartmentRoleDefinition> listByOrganization(Long organizationId);

    DepartmentRoleDefinition create(Long organizationId, String name, String color, boolean isLead, String permissions);

    DepartmentRoleDefinition update(Long id, String name, String color, Boolean isLead, String permissions);

    void deleteById(Long id);

    Optional<DepartmentRoleDefinition> findById(Long id);
}
