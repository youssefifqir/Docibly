package com.docibly.dms.dao.facade.core.department;

import com.docibly.dms.bean.core.department.DepartmentMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentMemberDao extends JpaRepository<DepartmentMember, Long>, JpaSpecificationExecutor<DepartmentMember> {

    List<DepartmentMember> findByDepartment_Id(Long departmentId);

    List<DepartmentMember> findByUser_Id(String userId);

    Optional<DepartmentMember> findByDepartment_IdAndUser_Id(Long departmentId, String userId);

    boolean existsByRole_Id(Long roleId);

    DepartmentMember findByRef(String ref);
    int deleteByRef(String ref);
}
