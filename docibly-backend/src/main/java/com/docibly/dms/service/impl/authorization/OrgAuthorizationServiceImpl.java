package com.docibly.dms.service.impl.authorization;

import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.organization.OrganizationMemberDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrgAuthorizationServiceImpl implements OrgAuthorizationService {

    private final OrganizationMemberDao organizationMemberDao;

    @Override
    @Transactional(readOnly = true)
    public void requireRole(Long orgId, String userId, MemberRole minRole) {
        OrganizationMember member = organizationMemberDao
                .findByOrganization_IdAndUser_Id(orgId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORG_MEMBER_NOT_FOUND));

        if (!OrgAuthorizationService.isAtLeast(member.getMemberRole(), minRole)) {
            throw new BusinessException(ErrorCode.ORG_ACCESS_DENIED);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void requireRole(Long orgId, MemberRole minRole) {
        String userId = getCurrentUserId();
        requireRole(orgId, userId, minRole);
    }

    @Override
    @Transactional(readOnly = true)
    public void requireOrgMember(Long orgId, String userId) {
        boolean isMember = organizationMemberDao
                .findByOrganization_IdAndUser_Id(orgId, userId)
                .isPresent();
        if (!isMember) {
            throw new BusinessException(ErrorCode.ORG_MEMBER_NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasRole(Long orgId, String userId, MemberRole minRole) {
        return organizationMemberDao
                .findByOrganization_IdAndUser_Id(orgId, userId)
                .map(member -> OrgAuthorizationService.isAtLeast(member.getMemberRole(), minRole))
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationMember getCurrentMember(Long orgId) {
        String userId = getCurrentUserId();
        return organizationMemberDao
                .findByOrganization_IdAndUser_Id(orgId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORG_MEMBER_NOT_FOUND));
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new BusinessException(ErrorCode.ORG_MEMBER_NOT_FOUND);
        }
        return user.getId();
    }
}
