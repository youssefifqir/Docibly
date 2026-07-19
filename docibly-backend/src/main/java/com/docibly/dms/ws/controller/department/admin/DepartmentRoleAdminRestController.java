package com.docibly.dms.ws.controller.department.admin;

import com.docibly.dms.bean.core.department.DepartmentRoleDefinition;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import com.docibly.dms.service.facade.department.DepartmentRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/department-roles")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class DepartmentRoleAdminRestController {

    private final DepartmentRoleService roleService;
    private final OrgAuthorizationService orgAuth;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> list(@RequestHeader("X-Org-Id") Long orgId) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        List<DepartmentRoleDefinition> roles = roleService.listByOrganization(orgId);
        return ResponseEntity.ok(roles.stream().map(this::toMap).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestBody Map<String, String> body) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        boolean isLead = Boolean.parseBoolean(body.getOrDefault("isLead", "false"));
        DepartmentRoleDefinition saved = roleService.create(orgId, body.get("name"), body.get("color"), isLead, body.get("permissions"));
        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        Boolean isLead = body.containsKey("isLead") ? Boolean.parseBoolean(body.get("isLead")) : null;
        DepartmentRoleDefinition saved = roleService.update(id, body.get("name"), body.get("color"), isLead, body.get("permissions"));
        return ResponseEntity.ok(toMap(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("X-Org-Id") Long orgId, @PathVariable Long id) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<Map<String, Object>>> listPermissionDefs(@RequestHeader("X-Org-Id") Long orgId) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        List<Map<String, Object>> groups = new ArrayList<>();

        Map<String, Object> docGroup = new HashMap<>();
        docGroup.put("group", "Documents");
        docGroup.put("perms", List.of(
            Map.of("id", "doc.view", "label", "View documents"),
            Map.of("id", "doc.upload", "label", "Upload documents"),
            Map.of("id", "doc.edit", "label", "Edit & annotate"),
            Map.of("id", "doc.download", "label", "Download"),
            Map.of("id", "doc.share", "label", "Share externally"),
            Map.of("id", "doc.delete", "label", "Delete documents")
        ));
        groups.add(docGroup);

        Map<String, Object> deptGroup = new HashMap<>();
        deptGroup.put("group", "Departments");
        deptGroup.put("perms", List.of(
            Map.of("id", "dept.create", "label", "Create sub-departments"),
            Map.of("id", "dept.members", "label", "Manage members"),
            Map.of("id", "dept.settings", "label", "Edit settings")
        ));
        groups.add(deptGroup);

        Map<String, Object> orgGroup = new HashMap<>();
        orgGroup.put("group", "Organization");
        orgGroup.put("perms", List.of(
            Map.of("id", "org.invite", "label", "Invite members"),
            Map.of("id", "org.roles", "label", "Manage roles"),
            Map.of("id", "org.audit", "label", "View audit log")
        ));
        groups.add(orgGroup);

        return ResponseEntity.ok(groups);
    }

    private Map<String, Object> toMap(DepartmentRoleDefinition r) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", r.getId());
        m.put("name", r.getName());
        m.put("color", r.getColor() != null ? r.getColor() : "");
        m.put("isLead", Boolean.TRUE.equals(r.getIsLead()));
        m.put("permissions", r.getPermissions());
        return m;
    }
}
