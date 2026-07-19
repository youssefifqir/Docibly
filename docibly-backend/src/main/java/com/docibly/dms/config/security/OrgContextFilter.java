package com.docibly.dms.config.security;

import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.organization.OrganizationMemberDao;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrgContextFilter extends OncePerRequestFilter {

    public static final String ORG_ID_HEADER = "X-Org-Id";
    public static final String ORG_CONTEXT_ATTR = "orgContext";

    private final OrganizationMemberDao organizationMemberDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String orgIdHeader = request.getHeader(ORG_ID_HEADER);
        if (orgIdHeader != null) {
            try {
                Long orgId = Long.parseLong(orgIdHeader);
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.getPrincipal() instanceof User user) {
                    OrganizationMember member = organizationMemberDao
                            .findByOrganization_IdAndUser_Id(orgId, user.getId())
                            .orElse(null);
                    if (member != null) {
                        request.setAttribute(ORG_CONTEXT_ATTR, member);
                    } else {
                        log.debug("User {} is not a member of org {}", user.getId(), orgId);
                    }
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid X-Org-Id header value: {}", orgIdHeader);
            }
        }

        chain.doFilter(request, response);
    }
}
