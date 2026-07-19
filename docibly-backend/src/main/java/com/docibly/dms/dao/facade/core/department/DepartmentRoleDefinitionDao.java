package com.docibly.dms.dao.facade.core.department;

import com.docibly.dms.bean.core.department.DepartmentRoleDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRoleDefinitionDao extends JpaRepository<DepartmentRoleDefinition, Long> {

    List<DepartmentRoleDefinition> findByOrganization_Id(Long organizationId);

    Optional<DepartmentRoleDefinition> findByOrganization_IdAndNameIgnoreCase(Long organizationId, String name);
}
