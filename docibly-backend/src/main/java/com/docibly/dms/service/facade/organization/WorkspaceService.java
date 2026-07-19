package com.docibly.dms.service.facade.organization;

import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.bean.core.organization.OrganizationMember;
import java.util.List;

public interface WorkspaceService {

    Organization createWorkspace(String name, String slug, String description);

    OrganizationInvitation inviteMember(Long orgId, String inviteeEmail, MemberRole intendedRole, Long departmentRoleId);

    OrganizationMember acceptInvitation(String token);

    OrganizationMember changeMemberRole(Long memberId, MemberRole newRole);

    void removeMember(Long memberId);

    List<OrganizationMember> listMembers(Long orgId);
}
