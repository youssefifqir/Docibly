package com.docibly.dms.ws.controller.organization.user;

import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.organization.OrganizationMemberDao;
import com.docibly.dms.service.facade.organization.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user/organizations")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class OrganizationFlowController {

    private final WorkspaceService workspaceService;
    private final OrganizationMemberDao organizationMemberDao;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentOrganization(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return organizationMemberDao.findByUser_Id(user.getId())
                .map(member -> {
                    Organization org = member.getOrganization();
                    return ResponseEntity.ok(Map.of(
                            "id", org.getId(),
                            "name", org.getName(),
                            "slug", org.getSlug(),
                            "role", member.getMemberRole().getDisplayText()
                    ));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No organization found for this user")));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Map<String, Object>>> listMyOrganizations(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<OrganizationMember> members = organizationMemberDao.findAllByUser_Id(user.getId());
        List<Map<String, Object>> owned = new ArrayList<>();
        List<Map<String, Object>> joined = new ArrayList<>();
        for (OrganizationMember m : members) {
            Organization org = m.getOrganization();
            Map<String, Object> item = Map.of(
                    "id", org.getId(),
                    "name", org.getName(),
                    "slug", org.getSlug(),
                    "role", m.getMemberRole().getDisplayText()
            );
            if (m.getMemberRole() == MemberRole.OWNER) {
                owned.add(item);
            } else {
                joined.add(item);
            }
        }
        return ResponseEntity.ok(List.of(
                Map.of("label", "My Organizations", "orgs", owned),
                Map.of("label", "Invited", "orgs", joined)
        ));
    }

    @PostMapping("/create-workspace")
    public ResponseEntity<Map<String, Object>> createWorkspace(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String slug = body.get("slug");
        String description = body.get("description");
        Organization org = workspaceService.createWorkspace(name, slug, description);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", org.getId(),
                "name", org.getName(),
                "slug", org.getSlug()
        ));
    }

    @PostMapping("/invite")
    public ResponseEntity<Map<String, Object>> inviteMember(
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestBody Map<String, String> body) {
        String email = body.get("email");
        MemberRole role = MemberRole.fromDisplayText(body.getOrDefault("role", "MEMBER"));
        Long departmentRoleId = body.containsKey("departmentRoleId") ? Long.parseLong(body.get("departmentRoleId")) : null;
        OrganizationInvitation invitation = workspaceService.inviteMember(orgId, email, role, departmentRoleId);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", invitation.getId(),
                "token", invitation.getToken(),
                "email", invitation.getInviteeEmail(),
                "role", invitation.getIntendedRole().getDisplayText(),
                "expiresAt", invitation.getExpiresAt().toString()
        ));
    }

    @PostMapping("/accept-invitation")
    public ResponseEntity<Map<String, Object>> acceptInvitation(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        OrganizationMember member = workspaceService.acceptInvitation(token);
        return ResponseEntity.ok(Map.of(
                "id", member.getId(),
                "orgId", member.getOrganization().getId(),
                "role", member.getMemberRole().getDisplayText()
        ));
    }

    @PutMapping("/members/{memberId}/role")
    public ResponseEntity<Map<String, Object>> changeRole(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long memberId,
            @RequestBody Map<String, String> body) {
        MemberRole newRole = MemberRole.fromDisplayText(body.get("role"));
        OrganizationMember member = workspaceService.changeMemberRole(memberId, newRole);
        return ResponseEntity.ok(Map.of(
                "id", member.getId(),
                "role", member.getMemberRole().getDisplayText()
        ));
    }

    @GetMapping("/members")
    public ResponseEntity<List<Map<String, Object>>> listMembers(@RequestHeader("X-Org-Id") Long orgId) {
        List<OrganizationMember> members = workspaceService.listMembers(orgId);
        List<Map<String, Object>> result = members.stream().map(m -> {
            User u = m.getUser();
            return Map.<String, Object>of(
                    "id", m.getId(),
                    "userId", u != null ? u.getId() : null,
                    "email", u != null ? u.getEmail() : null,
                    "firstName", u != null ? u.getFirstName() : null,
                    "lastName", u != null ? u.getLastName() : null,
                    "role", m.getMemberRole().getDisplayText(),
                    "joinedAt", m.getJoinedAt() != null ? m.getJoinedAt().toString() : null,
                    "isActive", m.getIsActive()
            );
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> removeMember(
            @RequestHeader("X-Org-Id") Long orgId,
            @PathVariable Long memberId) {
        workspaceService.removeMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
