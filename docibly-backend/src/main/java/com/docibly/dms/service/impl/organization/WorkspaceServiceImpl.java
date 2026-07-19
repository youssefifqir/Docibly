package com.docibly.dms.service.impl.organization;

import com.docibly.dms.bean.core.enums.InvitationStatus;
import com.docibly.dms.bean.core.enums.AuditAction;
import com.docibly.dms.bean.core.enums.MemberRole;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.bean.core.department.DepartmentRoleDefinition;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.config.storage.StorageProperties;
import com.docibly.dms.dao.facade.core.department.DepartmentRoleDefinitionDao;
import com.docibly.dms.dao.facade.core.organization.OrganizationDao;
import com.docibly.dms.dao.facade.core.organization.OrganizationInvitationDao;
import com.docibly.dms.dao.facade.core.organization.OrganizationMemberDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.auditlog.AuditLogger;
import com.docibly.dms.service.facade.authorization.OrgAuthorizationService;
import com.docibly.dms.service.facade.email.EmailService;
import com.docibly.dms.service.facade.notification.NotificationFiringService;
import com.docibly.dms.service.facade.organization.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    private final OrganizationDao organizationDao;
    private final OrganizationMemberDao organizationMemberDao;
    private final OrganizationInvitationDao organizationInvitationDao;
    private final DepartmentRoleDefinitionDao departmentRoleDefinitionDao;
    private final UserDao userDao;
    private final OrgAuthorizationService orgAuth;
    private final StorageProperties storageProperties;
    private final NotificationFiringService notificationFiringService;
    private final EmailService emailService;
    private final AuditLogger auditLogger;

    @Override
    @Transactional
    public Organization createWorkspace(String name, String slug, String description) {
        User currentUser = getCurrentUser();

        if (organizationDao.findBySlug(slug).isPresent()) {
            throw new BusinessException(ErrorCode.ORG_SLUG_TAKEN);
        }

        Organization organization = Organization.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .isActive(true)
                .maxMembers(50)
                .storageUsedBytes(0L)
                .storageQuotaBytes(storageProperties.getMaxFileSizeBytes() * 100)
                .planTier("FREE")
                .build();
        organization = organizationDao.save(organization);

        OrganizationMember owner = OrganizationMember.builder()
                .organization(organization)
                .user(currentUser)
                .memberRole(MemberRole.OWNER)
                .isActive(true)
                .joinedAt(LocalDateTime.now())
                .build();
        organizationMemberDao.save(owner);

        log.info("Workspace created: orgId={}, name={}, owner={}", organization.getId(), name, currentUser.getEmail());

        auditLogger.log(AuditAction.ORG_CREATED, "Organization", organization.getId().toString(),
                organization.getId(), organization.getName());

        return organization;
    }

    @Override
    @Transactional
    public OrganizationInvitation inviteMember(Long orgId, String inviteeEmail, MemberRole intendedRole, Long departmentRoleId) {
        orgAuth.requireRole(orgId, MemberRole.ADMIN);
        User currentUser = getCurrentUser();

        Organization organization = organizationDao.findById(orgId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORG_NOT_FOUND));

        if (intendedRole == MemberRole.OWNER) {
            throw new BusinessException(ErrorCode.ORG_INVALID_ROLE);
        }

        userDao.findByEmail(inviteeEmail).ifPresent(invitee -> {
            if (organizationMemberDao.findByOrganization_IdAndUser_Id(orgId, invitee.getId()).isPresent()) {
                throw new BusinessException(ErrorCode.ORG_MEMBER_ALREADY_EXISTS);
            }
        });

        DepartmentRoleDefinition departmentRole = null;
        if (departmentRoleId != null) {
            departmentRole = departmentRoleDefinitionDao.findById(departmentRoleId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_ROLE_NOT_FOUND));
        }

        OrganizationInvitation invitation = OrganizationInvitation.builder()
                .organization(organization)
                .invitedBy(currentUser)
                .inviteeEmail(inviteeEmail)
                .intendedRole(intendedRole)
                .departmentRole(departmentRole)
                .status(InvitationStatus.PENDING)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        invitation = organizationInvitationDao.save(invitation);

        userDao.findByEmail(inviteeEmail).ifPresent(invitee ->
                notificationFiringService.inviteReceived(invitee.getId(),
                        organization.getName(), currentUser.getEmail())
        );

        emailService.sendInvitationEmail(inviteeEmail, organization.getName(), currentUser.getEmail(), invitation.getToken());

        log.info("Invitation sent: orgId={}, email={}, role={}, token={}",
                orgId, inviteeEmail, intendedRole, invitation.getToken());

        auditLogger.log(AuditAction.MEMBER_INVITED, "OrganizationInvitation", invitation.getId().toString(),
                orgId, "invitee=" + inviteeEmail + ", role=" + intendedRole);

        return invitation;
    }

    @Override
    @Transactional
    public OrganizationMember acceptInvitation(String token) {
        User currentUser = getCurrentUser();

        OrganizationInvitation invitation = organizationInvitationDao.findByToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORG_INVITATION_NOT_FOUND));

        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new BusinessException(ErrorCode.ORG_INVITATION_EXPIRED);
        }
        if (invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            invitation.setStatus(InvitationStatus.EXPIRED);
            organizationInvitationDao.save(invitation);
            throw new BusinessException(ErrorCode.ORG_INVITATION_EXPIRED);
        }
        if (!invitation.getInviteeEmail().equalsIgnoreCase(currentUser.getEmail())) {
            throw new BusinessException(ErrorCode.ORG_INVITATION_WRONG_USER);
        }

        if (organizationMemberDao.findByOrganization_IdAndUser_Id(
                invitation.getOrganization().getId(), currentUser.getId()).isPresent()) {
            throw new BusinessException(ErrorCode.ORG_MEMBER_ALREADY_EXISTS);
        }

        OrganizationMember member = OrganizationMember.builder()
                .organization(invitation.getOrganization())
                .user(currentUser)
                .memberRole(invitation.getIntendedRole())
                .isActive(true)
                .joinedAt(LocalDateTime.now())
                .invitedByEmail(invitation.getInvitedBy().getEmail())
                .build();
        member = organizationMemberDao.save(member);

        organizationMemberDao.findByOrganization_IdAndUser_Id(
                invitation.getOrganization().getId(), invitation.getInvitedBy().getId()
        ).ifPresent(inviter ->
                notificationFiringService.memberJoined(inviter.getUser().getId(),
                        invitation.getOrganization().getName(), currentUser.getEmail())
        );

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setAcceptedAt(LocalDateTime.now());
        organizationInvitationDao.save(invitation);

        log.info("Invitation accepted: orgId={}, userId={}, role={}",
                invitation.getOrganization().getId(), currentUser.getId(), member.getMemberRole());
        return member;
    }

    @Override
    @Transactional
    public OrganizationMember changeMemberRole(Long memberId, MemberRole newRole) {
        OrganizationMember member = organizationMemberDao.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORG_MEMBER_NOT_FOUND));

        Long orgId = member.getOrganization().getId();
        orgAuth.requireRole(orgId, MemberRole.ADMIN);

        if (member.getMemberRole() == MemberRole.OWNER) {
            orgAuth.requireRole(orgId, MemberRole.OWNER);
        }

        User currentUser = getCurrentUser();
        OrganizationMember currentMember = organizationMemberDao
                .findByOrganization_IdAndUser_Id(orgId, currentUser.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORG_MEMBER_NOT_FOUND));

        if (currentMember.getMemberRole().ordinal() > newRole.ordinal()) {
            throw new BusinessException(ErrorCode.ORG_INSUFFICIENT_PERMISSIONS);
        }

        if (newRole == MemberRole.OWNER) {
            orgAuth.requireRole(orgId, MemberRole.OWNER);
        }

        member.setMemberRole(newRole);
        member = organizationMemberDao.save(member);

        log.info("Member role changed: memberId={}, orgId={}, newRole={}", memberId, orgId, newRole);
        return member;
    }

    @Override
    @Transactional
    public void removeMember(Long memberId) {
        OrganizationMember member = organizationMemberDao.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORG_MEMBER_NOT_FOUND));

        Long orgId = member.getOrganization().getId();
        orgAuth.requireRole(orgId, MemberRole.ADMIN);

        if (member.getMemberRole() == MemberRole.OWNER) {
            orgAuth.requireRole(orgId, MemberRole.OWNER);
        }

        User currentUser = getCurrentUser();
        if (member.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException(ErrorCode.ORG_CANNOT_REMOVE_SELF);
        }

        String removedEmail = member.getUser().getEmail();
        member.setDeletedAt(LocalDateTime.now());
        organizationMemberDao.save(member);

        auditLogger.log(AuditAction.MEMBER_REMOVED, "OrganizationMember", memberId.toString(),
                orgId, "removedUser=" + removedEmail);

        log.info("Member removed: memberId={}, orgId={}", memberId, orgId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationMember> listMembers(Long orgId) {
        orgAuth.requireRole(orgId, MemberRole.VIEWER);
        return organizationMemberDao.findByOrganization_Id(orgId);
    }

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new BusinessException(ErrorCode.ORG_MEMBER_NOT_FOUND);
        }
        return userDao.findById(user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
