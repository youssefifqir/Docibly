package com.docibly.dms.service.impl.department;

import com.docibly.dms.bean.core.department.DepartmentRoleDefinition;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.dao.facade.core.department.DepartmentMemberDao;
import com.docibly.dms.dao.facade.core.department.DepartmentRoleDefinitionDao;
import com.docibly.dms.dao.facade.core.organization.OrganizationDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.department.DepartmentRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentRoleServiceImpl implements DepartmentRoleService {

    private static final List<String> DEFAULT_ROLE_NAMES = List.of("Manager", "Member");

    private final DepartmentRoleDefinitionDao dao;
    private final OrganizationDao organizationDao;
    private final DepartmentMemberDao departmentMemberDao;

    @Override
    @Transactional
    public List<DepartmentRoleDefinition> listByOrganization(Long organizationId) {
        List<DepartmentRoleDefinition> roles = dao.findByOrganization_Id(organizationId);
        if (roles.isEmpty()) {
            Organization org = organizationDao.findById(organizationId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORG_NOT_FOUND));
            boolean first = true;
            for (String name : DEFAULT_ROLE_NAMES) {
                dao.save(DepartmentRoleDefinition.builder().name(name).organization(org).isLead(first).build());
                first = false;
            }
            roles = dao.findByOrganization_Id(organizationId);
        }
        return roles;
    }

    @Override
    @Transactional
    public DepartmentRoleDefinition create(Long organizationId, String name, String color, boolean isLead, String permissions) {
        if (name == null || name.isBlank()) {
            throw new BusinessException(ErrorCode.DEPT_ROLE_NOT_FOUND);
        }
        Organization org = organizationDao.findById(organizationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORG_NOT_FOUND));
        dao.findByOrganization_IdAndNameIgnoreCase(organizationId, name.trim()).ifPresent(existing -> {
            throw new BusinessException(ErrorCode.DEPT_ROLE_DUPLICATE);
        });
        DepartmentRoleDefinition saved = dao.save(DepartmentRoleDefinition.builder()
                .name(name.trim())
                .color(color)
                .isLead(isLead)
                .permissions(permissions)
                .organization(org)
                .build());
        log.info("Department role created: id={}, name={}, orgId={}", saved.getId(), saved.getName(), organizationId);
        return saved;
    }

    @Override
    @Transactional
    public DepartmentRoleDefinition update(Long id, String name, String color, Boolean isLead, String permissions) {
        DepartmentRoleDefinition role = dao.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_ROLE_NOT_FOUND));
        if (name != null && !name.isBlank()) role.setName(name.trim());
        if (color != null) role.setColor(color);
        if (isLead != null) role.setIsLead(isLead);
        if (permissions != null) role.setPermissions(permissions);
        DepartmentRoleDefinition saved = dao.save(role);
        log.info("Department role updated: id={}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        DepartmentRoleDefinition role = dao.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_ROLE_NOT_FOUND));
        if (departmentMemberDao.existsByRole_Id(id)) {
            throw new BusinessException(ErrorCode.DEPT_ROLE_IN_USE);
        }
        role.setDeletedAt(LocalDateTime.now());
        dao.save(role);
        log.info("Department role deleted: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepartmentRoleDefinition> findById(Long id) {
        return dao.findById(id);
    }
}
