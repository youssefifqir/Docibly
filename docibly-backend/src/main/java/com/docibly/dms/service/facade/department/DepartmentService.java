package com.docibly.dms.service.facade.department;

import com.docibly.dms.bean.core.department.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    Department create(Department department);

    Department update(Department department);

    Department changeParent(Long departmentId, Long newParentId);

    Optional<Department> findById(Long id);

    List<Department> findByOrganizationId(Long organizationId);

    void deleteById(Long id);

    List<Department> findAll();
}
