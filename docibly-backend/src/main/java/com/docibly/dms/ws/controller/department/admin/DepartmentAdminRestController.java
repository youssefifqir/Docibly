package com.docibly.dms.ws.controller.department.admin;

import com.docibly.dms.bean.core.department.Department;
import com.docibly.dms.bean.core.department.DepartmentMember;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import com.docibly.dms.service.facade.department.DepartmentMemberService;
import com.docibly.dms.service.facade.department.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/departments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class DepartmentAdminRestController {

    private final DepartmentService departmentService;
    private final DepartmentMemberService departmentMemberService;
    private final OrgAuthorizationService orgAuth;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> list(
            @RequestHeader("X-Org-Id") Long orgId) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        List<Department> departments = departmentService.findByOrganizationId(orgId);
        List<Map<String, Object>> result = departments.stream().map(dept -> {
            Map<String, Object> m = new java.util.HashMap<>();
            m.put("id", dept.getId());
            m.put("name", dept.getName());
            m.put("description", dept.getDescription() != null ? dept.getDescription() : "");
            m.put("color", dept.getColor() != null ? dept.getColor() : "");
            m.put("isActive", dept.getIsActive() != null ? dept.getIsActive() : true);
            m.put("memberCount", dept.getMembers() != null ? dept.getMembers().size() : 0);
            m.put("subDepartmentCount", dept.getSubDepartments() != null ? dept.getSubDepartments().size() : 0);
            m.put("parentId", dept.getParentDepartment() != null ? dept.getParentDepartment().getId() : null);
            m.put("parentName", dept.getParentDepartment() != null ? dept.getParentDepartment().getName() : null);

            DepartmentMember lead = dept.getMembers() == null ? null : dept.getMembers().stream()
                    .filter(dm -> dm.getRole() != null && Boolean.TRUE.equals(dm.getRole().getIsLead()))
                    .findFirst()
                    .orElseGet(() -> dept.getMembers().stream().findFirst().orElse(null));
            if (lead != null && lead.getUser() != null) {
                String fullName = (lead.getUser().getFirstName() + " " + lead.getUser().getLastName()).trim();
                m.put("responsibleName", fullName.isBlank() ? lead.getUser().getEmail() : fullName);
                m.put("responsibleEmail", lead.getUser().getEmail());
            } else {
                m.put("responsibleName", null);
                m.put("responsibleEmail", null);
            }
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestBody Map<String, String> body) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        Department dept = Department.builder()
                .name(body.get("name"))
                .description(body.get("description"))
                .color(body.get("color"))
                .build();
        dept.setOrganization(new com.docibly.dms.bean.core.organization.Organization());
        dept.getOrganization().setId(orgId);
        String parentIdStr = body.get("parentId");
        if (parentIdStr != null && !parentIdStr.isBlank()) {
            Department parentRef = new Department();
            parentRef.setId(Long.valueOf(parentIdStr));
            dept.setParentDepartment(parentRef);
        }
        Department saved = departmentService.create(dept);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", saved.getId(),
                "name", saved.getName(),
                "description", saved.getDescription() != null ? saved.getDescription() : "",
                "color", saved.getColor() != null ? saved.getColor() : ""
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long id) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        return departmentService.findById(id)
                .map(dept -> ResponseEntity.ok(Map.<String, Object>of(
                        "id", dept.getId(),
                        "name", dept.getName(),
                        "description", dept.getDescription() != null ? dept.getDescription() : "",
                        "color", dept.getColor() != null ? dept.getColor() : "",
                        "isActive", dept.getIsActive() != null ? dept.getIsActive() : true
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        Department dept = new Department();
        dept.setId(id);
        if (body.containsKey("name")) dept.setName(body.get("name"));
        if (body.containsKey("description")) dept.setDescription(body.get("description"));
        if (body.containsKey("color")) dept.setColor(body.get("color"));
        if (body.containsKey("isActive")) dept.setIsActive(Boolean.parseBoolean(body.get("isActive")));
        Department saved = departmentService.update(dept);
        if (body.containsKey("parentId")) {
            String parentIdStr = body.get("parentId");
            Long newParentId = (parentIdStr == null || parentIdStr.isBlank()) ? null : Long.valueOf(parentIdStr);
            saved = departmentService.changeParent(id, newParentId);
        }
        return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "name", saved.getName(),
                "description", saved.getDescription() != null ? saved.getDescription() : "",
                "color", saved.getColor() != null ? saved.getColor() : ""
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long id) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        departmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<Map<String, Object>>> listMembers(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long id) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        List<DepartmentMember> members = departmentMemberService.findByDepartmentId(id);
        List<Map<String, Object>> result = members.stream().map(m -> {
            var user = m.getUser();
            return Map.<String, Object>of(
                    "id", m.getId(),
                    "userId", user != null ? user.getId() : null,
                    "email", user != null ? user.getEmail() : null,
                    "firstName", user != null ? user.getFirstName() : null,
                    "lastName", user != null ? user.getLastName() : null,
                    "roleId", m.getRole().getId(),
                    "role", m.getRole().getName(),
                    "joinedAt", m.getJoinedAt() != null ? m.getJoinedAt().toString() : null
            );
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Map<String, Object>> addMember(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        String userId = body.get("userId");
        Long roleId = Long.valueOf(body.get("roleId"));
        DepartmentMember member = departmentMemberService.addMember(id, userId, roleId);
        var user = member.getUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", member.getId(),
                "userId", user != null ? user.getId() : null,
                "role", member.getRole().getName()
        ));
    }

    @PutMapping("/{deptId}/members/{memberId}/role")
    public ResponseEntity<Map<String, Object>> changeMemberRole(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long deptId,
            @PathVariable Long memberId,
            @RequestBody Map<String, String> body) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        Long newRoleId = Long.valueOf(body.get("roleId"));
        DepartmentMember member = departmentMemberService.changeRole(memberId, newRoleId);
        return ResponseEntity.ok(Map.of(
                "id", member.getId(),
                "role", member.getRole().getName()
        ));
    }

    @DeleteMapping("/{deptId}/members/{memberId}")
    public ResponseEntity<Void> removeMember(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long deptId,
            @PathVariable Long memberId) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        departmentMemberService.removeMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
