package com.docibly.dms.service.facade.authorization;

import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.organization.OrganizationMember;

public interface OrgAuthorizationService {

    void requireRole(Long orgId, String userId, MemberRole minRole);

    void requireRole(Long orgId, MemberRole minRole);

    void requireOrgMember(Long orgId, String userId);

    boolean hasRole(Long orgId, String userId, MemberRole minRole);

    OrganizationMember getCurrentMember(Long orgId);

    static boolean isAtLeast(MemberRole actual, MemberRole minimum) {
        if (actual == null || minimum == null) return false;
        return actual.ordinal() <= minimum.ordinal();
    }
}
