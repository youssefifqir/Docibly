package com.docibly.dms.ws.controller.user;

import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.service.facade.department.DepartmentPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user/me")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class UserPermissionsController {

    private final DepartmentPermissionService permissionService;

    /**
     * Returns the union of all department-role permissions for the current user
     * in the given org. Org ADMIN/OWNER get the full set.
     *
     * GET /api/v1/user/me/dept-permissions  (X-Org-Id header required)
     * Response: { "permissions": ["doc.view", "doc.upload", ...] }
     */
    @GetMapping("/dept-permissions")
    public ResponseEntity<Map<String, Object>> getDeptPermissions(
            @RequestHeader("X-Org-Id") Long orgId,
            Authentication auth) {
        if (!(auth.getPrincipal() instanceof User user)) {
            return ResponseEntity.ok(Map.of("permissions", Set.of()));
        }
        Set<String> perms = permissionService.getEffectivePermissions(user.getId(), orgId);
        return ResponseEntity.ok(Map.of("permissions", perms));
    }
}
