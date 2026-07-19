package com.docibly.dms.service.impl.department;

import com.docibly.dms.bean.core.department.Department;
import com.docibly.dms.bean.core.department.DepartmentDocumentShare;
import com.docibly.dms.bean.core.department.DepartmentMember;
import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.enums.SharePermission;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.department.DepartmentDao;
import com.docibly.dms.dao.facade.core.department.DepartmentDocumentShareDao;
import com.docibly.dms.dao.facade.core.department.DepartmentMemberDao;
import com.docibly.dms.dao.facade.core.document.DocumentDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.department.DepartmentDocumentShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentDocumentShareServiceImpl implements DepartmentDocumentShareService {

    private final DepartmentDocumentShareDao dao;
    private final DepartmentDao departmentDao;
    private final DocumentDao documentDao;
    private final UserDao userDao;
    private final DepartmentMemberDao departmentMemberDao;

    @Override
    @Transactional
    public DepartmentDocumentShare shareDocument(Long departmentId, Long documentId,
                                                  SharePermission permission, String sharedByUserId) {
        Department department = departmentDao.findById(departmentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_NOT_FOUND));
        Document document = documentDao.findById(documentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DOCUMENT_NOT_FOUND));
        User sharedBy = userDao.findById(sharedByUserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        DepartmentDocumentShare share = DepartmentDocumentShare.builder()
                .department(department)
                .document(document)
                .sharedBy(sharedBy)
                .permission(permission)
                .sharedAt(LocalDateTime.now())
                .build();
        DepartmentDocumentShare saved = dao.save(share);
        log.info("Document shared with department: deptId={}, docId={}, permission={}, byUserId={}",
                departmentId, documentId, permission, sharedByUserId);
        return saved;
    }

    @Override
    @Transactional
    public DepartmentDocumentShare updatePermission(Long shareId, SharePermission permission) {
        DepartmentDocumentShare share = dao.findById(shareId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEPT_SHARE_NOT_FOUND));
        share.setPermission(permission);
        DepartmentDocumentShare saved = dao.save(share);
        log.info("Department share permission updated: shareId={}, permission={}", shareId, permission);
        return saved;
    }

    @Override
    @Transactional
    public void removeShare(Long shareId) {
        dao.findById(shareId).ifPresent(share -> {
            share.setDeletedAt(LocalDateTime.now());
            dao.save(share);
            log.info("Department share removed: shareId={}", shareId);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDocumentShare> findByDepartmentId(Long departmentId) {
        return dao.findByDepartment_Id(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDocumentShare> findByOrganizationId(Long organizationId) {
        return dao.findByDepartment_Organization_Id(organizationId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDocumentShare> findByDocumentId(Long documentId) {
        return dao.findByDocument_Id(documentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDocumentShare> findSharedWithUserDepartments(String userId) {
        List<DepartmentMember> memberships = departmentMemberDao.findByUser_Id(userId);
        List<Long> departmentIds = memberships.stream()
                .map(m -> m.getDepartment().getId())
                .collect(Collectors.toList());
        return departmentIds.stream()
                .flatMap(deptId -> dao.findByDepartment_Id(deptId).stream())
                .collect(Collectors.toList());
    }
}
