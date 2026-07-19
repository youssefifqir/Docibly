package com.docibly.dms.service.impl.department;

import com.docibly.dms.bean.core.department.DepartmentMember;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.department.DepartmentMemberDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import com.docibly.dms.service.facade.department.DepartmentPermissionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentPermissionServiceImpl implements DepartmentPermissionService {

    /** Full set of known permission IDs — returned for org ADMIN/OWNER users. */
    private static final Set<String> ALL_PERMISSIONS = Set.of(
        "doc.view", "doc.upload", "doc.edit", "doc.download", "doc.share", "doc.delete",
        "dept.create", "dept.members", "dept.settings",
        "org.invite", "org.roles", "org.audit"
    );

    private final DepartmentMemberDao departmentMemberDao;
    private final OrgAuthorizationService orgAuth;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public Set<String> getEffectivePermissions(String userId, Long orgId) {
        // Org ADMIN or OWNER bypasses all department-role restrictions
        if (orgAuth.hasRole(orgId, userId, MemberRole.ADMIN)) {
            return ALL_PERMISSIONS;
        }

        List<DepartmentMember> memberships = departmentMemberDao.findByUser_Id(userId);
        Set<String> perms = new HashSet<>();

        for (DepartmentMember m : memberships) {
            if (m.getDepartment() == null
                    || m.getDepartment().getOrganization() == null
                    || !orgId.equals(m.getDepartment().getOrganization().getId())) {
                continue;
            }
            if (m.getRole() == null) continue;

            String permJson = m.getRole().getPermissions();
            if (permJson != null && !permJson.isBlank()) {
                try {
                    List<String> rolePerms = objectMapper.readValue(permJson, new TypeReference<>() {});
                    perms.addAll(rolePerms);
                } catch (Exception e) {
                    log.warn("Failed to parse permissions for role id={}: {}", m.getRole().getId(), e.getMessage());
                }
            }
        }
        return perms;
    }

    @Override
    @Transactional(readOnly = true)
    public void requirePermission(Long orgId, String permissionId) {
        String userId = getCurrentUserId();
        if (!getEffectivePermissions(userId, orgId).contains(permissionId)) {
            log.warn("Permission denied: user={} org={} perm={}", userId, orgId, permissionId);
            throw new BusinessException(ErrorCode.DEPT_PERMISSION_DENIED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean currentUserHasPermission(Long orgId, String permissionId) {
        try {
            String userId = getCurrentUserId();
            return getEffectivePermissions(userId, orgId).contains(permissionId);
        } catch (Exception e) {
            return false;
        }
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new BusinessException(ErrorCode.ORG_MEMBER_NOT_FOUND);
        }
        return user.getId();
    }
}
