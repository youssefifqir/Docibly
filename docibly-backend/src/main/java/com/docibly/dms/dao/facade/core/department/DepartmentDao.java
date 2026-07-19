package com.docibly.dms.dao.facade.core.department;

import com.docibly.dms.bean.core.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentDao extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {

    List<Department> findByOrganization_Id(Long organizationId);

    List<Department> findByParentDepartment_Id(Long parentDepartmentId);

    Department findByRef(String ref);
    int deleteByRef(String ref);
}
