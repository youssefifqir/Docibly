package com.docibly.dms.service.impl.department;

import com.docibly.dms.bean.core.department.Department;
import com.docibly.dms.bean.core.department.DepartmentMember;
import com.docibly.dms.bean.core.department.DepartmentRoleDefinition;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.department.DepartmentDao;
import com.docibly.dms.dao.facade.core.department.DepartmentMemberDao;
import com.docibly.dms.dao.facade.core.department.DepartmentRoleDefinitionDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.department.DepartmentMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentMemberServiceImpl implements DepartmentMemberService {

    private final DepartmentMemberDao dao;
    private final DepartmentDao departmentDao;
    private final UserDao userDao;
    private final DepartmentRoleDefinitionDao roleDao;

    @Override
    @Transactional
    public DepartmentMember addMember(Long departmentId, String userId, Long roleId) {
        Department department = departmentDao.findById(departmentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_NOT_FOUND));
        User user = userDao.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        DepartmentRoleDefinition role = roleDao.findById(roleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_ROLE_NOT_FOUND));

        if (dao.findByDepartment_IdAndUser_Id(departmentId, userId).isPresent()) {
            throw new BusinessException(ErrorCode.DEPT_MEMBER_ALREADY_EXISTS);
        }

        DepartmentMember member = DepartmentMember.builder()
                .department(department)
                .user(user)
                .role(role)
                .joinedAt(LocalDateTime.now())
                .build();
        DepartmentMember saved = dao.save(member);
        log.info("Member added to department: deptId={}, userId={}, roleId={}", departmentId, userId, roleId);
        return saved;
    }

    @Override
    @Transactional
    public DepartmentMember changeRole(Long memberId, Long newRoleId) {
        DepartmentMember member = dao.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_MEMBER_NOT_FOUND));
        DepartmentRoleDefinition role = roleDao.findById(newRoleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_ROLE_NOT_FOUND));
        member.setRole(role);
        DepartmentMember saved = dao.save(member);
        log.info("Department member role changed: memberId={}, newRoleId={}", memberId, newRoleId);
        return saved;
    }

    @Override
    @Transactional
    public void removeMember(Long memberId) {
        dao.findById(memberId).ifPresent(member -> {
            member.setDeletedAt(LocalDateTime.now());
            dao.save(member);
            log.info("Department member removed: memberId={}", memberId);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentMember> findByDepartmentId(Long departmentId) {
        return dao.findByDepartment_Id(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentMember> findByUserId(String userId) {
        return dao.findByUser_Id(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentMember> findByDepartmentAndUser(Long departmentId, String userId) {
        return dao.findByDepartment_IdAndUser_Id(departmentId, userId);
    }
}
