package com.docibly.dms.ws.controller.department.user;

import com.docibly.dms.bean.core.department.Department;
import com.docibly.dms.bean.core.department.DepartmentDocumentShare;
import com.docibly.dms.bean.core.department.DepartmentMember;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.service.facade.department.DepartmentDocumentShareService;
import com.docibly.dms.service.facade.department.DepartmentMemberService;
import com.docibly.dms.service.facade.department.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user/departments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class DepartmentUserRestController {

    private final DepartmentService departmentService;
    private final DepartmentMemberService departmentMemberService;
    private final DepartmentDocumentShareService shareService;

    @GetMapping("/my")
    public ResponseEntity<List<Map<String, Object>>> myDepartments(
            @RequestHeader("X-Org-Id") Long orgId,
            Authentication auth) {
        if (!(auth.getPrincipal() instanceof User user)) {
            return ResponseEntity.ok(List.of());
        }
        List<DepartmentMember> memberships = departmentMemberService.findByUserId(user.getId());
        List<Map<String, Object>> result = memberships.stream()
                .filter(m -> m.getDepartment().getOrganization().getId().equals(orgId))
                .map(m -> {
                    Department d = m.getDepartment();
                    return Map.<String, Object>of(
                            "id", d.getId(),
                            "name", d.getName(),
                            "color", d.getColor() != null ? d.getColor() : "",
                            "role", m.getRole().getName(),
                            "memberCount", d.getMembers() != null ? d.getMembers().size() : 0
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/shared-docs")
    public ResponseEntity<List<Map<String, Object>>> sharedWithMyDepartments(
            @RequestHeader("X-Org-Id") Long orgId,
            Authentication auth) {
        if (!(auth.getPrincipal() instanceof User user)) {
            return ResponseEntity.ok(List.of());
        }
        List<DepartmentDocumentShare> shares = shareService.findSharedWithUserDepartments(user.getId());
        List<Map<String, Object>> result = shares.stream()
                .filter(s -> s.getDepartment().getOrganization().getId().equals(orgId))
                .map(s -> {
                    var doc = s.getDocument();
                    return Map.<String, Object>of(
                            "id", s.getId(),
                            "documentId", doc != null ? doc.getId() : null,
                            "documentTitle", doc != null ? doc.getTitle() : null,
                            "permission", s.getPermission().getDisplayText(),
                            "departmentName", s.getDepartment().getName(),
                            "sharedAt", s.getSharedAt() != null ? s.getSharedAt().toString() : null
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
