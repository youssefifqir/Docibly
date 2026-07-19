package com.docibly.dms.ws.controller.department.admin;

import com.docibly.dms.bean.core.department.DepartmentDocumentShare;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.enums.SharePermission;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import com.docibly.dms.service.facade.department.DepartmentDocumentShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/departments/{deptId}/shared-docs")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class DepartmentDocumentShareAdminRestController {

    private final DepartmentDocumentShareService shareService;
    private final OrgAuthorizationService orgAuth;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listSharedDocs(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long deptId) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        List<DepartmentDocumentShare> shares = shareService.findByDepartmentId(deptId);
        List<Map<String, Object>> result = shares.stream().map(s -> {
            var doc = s.getDocument();
            var sharedBy = s.getSharedBy();
            return Map.<String, Object>of(
                    "id", s.getId(),
                    "documentId", doc != null ? doc.getId() : null,
                    "documentTitle", doc != null ? doc.getTitle() : null,
                    "permission", s.getPermission().getDisplayText(),
                    "sharedBy", sharedBy != null ? sharedBy.getEmail() : null,
                    "sharedAt", s.getSharedAt() != null ? s.getSharedAt().toString() : null,
                    "expiresAt", s.getExpiresAt() != null ? s.getExpiresAt().toString() : null
            );
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> shareDocument(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long deptId,
            @RequestBody Map<String, Object> body) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        Long documentId = Long.valueOf(body.get("documentId").toString());
        SharePermission permission = SharePermission.fromDisplayText(
                body.getOrDefault("permission", "Can View").toString());
        String sharedByUserId = body.get("sharedByUserId").toString();

        DepartmentDocumentShare share = shareService.shareDocument(deptId, documentId, permission, sharedByUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", share.getId(),
                "documentId", share.getDocument().getId(),
                "permission", share.getPermission().getDisplayText()
        ));
    }

    @PutMapping("/{shareId}")
    public ResponseEntity<Map<String, Object>> updatePermission(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long deptId,
            @PathVariable Long shareId,
            @RequestBody Map<String, String> body) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        SharePermission permission = SharePermission.fromDisplayText(body.get("permission"));
        DepartmentDocumentShare share = shareService.updatePermission(shareId, permission);
        return ResponseEntity.ok(Map.of(
                "id", share.getId(),
                "permission", share.getPermission().getDisplayText()
        ));
    }

    @DeleteMapping("/{shareId}")
    public ResponseEntity<Void> removeShare(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long deptId,
            @PathVariable Long shareId) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        shareService.removeShare(shareId);
        return ResponseEntity.noContent().build();
    }
}
