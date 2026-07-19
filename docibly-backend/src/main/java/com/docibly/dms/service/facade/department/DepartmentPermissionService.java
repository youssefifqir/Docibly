package com.docibly.dms.service.facade.department;

import java.util.Set;

public interface DepartmentPermissionService {

    /**
     * Union of all department-role permissions this user holds in the given org.
     * Org ADMIN/OWNER always returns the full set.
     */
    Set<String> getEffectivePermissions(String userId, Long orgId);

    /**
     * Throws DEPT_PERMISSION_DENIED (403) if the current authenticated user
     * does not have the given permission in the org.
     */
    void requirePermission(Long orgId, String permissionId);

    /** Same check but returns a boolean instead of throwing. */
    boolean currentUserHasPermission(Long orgId, String permissionId);
}
