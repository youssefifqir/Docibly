package com.docibly.dms.service.impl.department;

import com.docibly.dms.bean.core.department.Department;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.dao.facade.core.department.DepartmentDao;
import com.docibly.dms.dao.facade.core.organization.OrganizationDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.department.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentDao dao;
    private final OrganizationDao organizationDao;

    @Override
    @Transactional
    public Department create(Department department) {
        if (department.getOrganization() == null || department.getOrganization().getId() == null) {
            throw new BusinessException(ErrorCode.ORG_NOT_FOUND);
        }
        Organization org = organizationDao.findById(department.getOrganization().getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORG_NOT_FOUND));
        department.setOrganization(org);

        if (department.getParentDepartment() != null && department.getParentDepartment().getId() != null) {
            Department parent = dao.findById(department.getParentDepartment().getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_NOT_FOUND));
            if (!parent.getOrganization().getId().equals(org.getId())) {
                throw new BusinessException(ErrorCode.DEPT_INVALID_PARENT);
            }
            department.setParentDepartment(parent);
        } else {
            department.setParentDepartment(null);
        }

        if (department.getIsActive() == null) {
            department.setIsActive(true);
        }
        Department saved = dao.save(department);
        log.info("Department created: id={}, name={}, orgId={}, parentId={}",
                saved.getId(), saved.getName(), org.getId(),
                saved.getParentDepartment() != null ? saved.getParentDepartment().getId() : null);
        return saved;
    }

    @Override
    @Transactional
    public Department update(Department department) {
        Department existing = dao.findById(department.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_NOT_FOUND));
        if (department.getName() != null) {
            existing.setName(department.getName());
        }
        if (department.getDescription() != null) {
            existing.setDescription(department.getDescription());
        }
        if (department.getColor() != null) {
            existing.setColor(department.getColor());
        }
        if (department.getIsActive() != null) {
            existing.setIsActive(department.getIsActive());
        }
        Department saved = dao.save(existing);
        log.info("Department updated: id={}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Department changeParent(Long departmentId, Long newParentId) {
        Department existing = dao.findById(departmentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_NOT_FOUND));
        if (newParentId == null) {
            existing.setParentDepartment(null);
        } else if (newParentId.equals(departmentId)) {
            throw new BusinessException(ErrorCode.DEPT_INVALID_PARENT);
        } else {
            Department newParent = dao.findById(newParentId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_NOT_FOUND));
            if (!newParent.getOrganization().getId().equals(existing.getOrganization().getId())) {
                throw new BusinessException(ErrorCode.DEPT_INVALID_PARENT);
            }
            validateNoCycle(departmentId, newParent);
            existing.setParentDepartment(newParent);
        }
        Department saved = dao.save(existing);
        log.info("Department reparented: id={}, newParentId={}", saved.getId(), newParentId);
        return saved;
    }

    private void validateNoCycle(Long deptId, Department newParent) {
        Department cursor = newParent;
        while (cursor != null) {
            if (cursor.getId().equals(deptId)) {
                throw new BusinessException(ErrorCode.DEPT_INVALID_PARENT);
            }
            cursor = cursor.getParentDepartment();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Department> findById(Long id) {
        return dao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> findByOrganizationId(Long organizationId) {
        return dao.findByOrganization_Id(organizationId);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        dao.findById(id).ifPresent(dept -> {
            if (!dao.findByParentDepartment_Id(id).isEmpty()) {
                throw new BusinessException(ErrorCode.DEPT_HAS_SUBDEPARTMENTS);
            }
            dept.setDeletedAt(LocalDateTime.now());
            dao.save(dept);
            log.info("Department soft-deleted: id={}", id);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> findAll() {
        return dao.findAll();
    }
}
