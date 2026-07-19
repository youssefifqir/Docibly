package com.docibly.dms.service.facade.department;

import com.docibly.dms.bean.core.department.DepartmentMember;

import java.util.List;
import java.util.Optional;

public interface DepartmentMemberService {

    DepartmentMember addMember(Long departmentId, String userId, Long roleId);

    DepartmentMember changeRole(Long memberId, Long newRoleId);

    void removeMember(Long memberId);

    List<DepartmentMember> findByDepartmentId(Long departmentId);

    List<DepartmentMember> findByUserId(String userId);

    Optional<DepartmentMember> findByDepartmentAndUser(Long departmentId, String userId);
}
