package com.docibly.dms.service.facade.department;

import com.docibly.dms.bean.core.department.DepartmentDocumentShare;
import com.docibly.dms.bean.core.enums.SharePermission;

import java.util.List;

public interface DepartmentDocumentShareService {

    DepartmentDocumentShare shareDocument(Long departmentId, Long documentId, SharePermission permission, String sharedByUserId);

    DepartmentDocumentShare updatePermission(Long shareId, SharePermission permission);

    void removeShare(Long shareId);

    List<DepartmentDocumentShare> findByDepartmentId(Long departmentId);

    List<DepartmentDocumentShare> findByOrganizationId(Long organizationId);

    List<DepartmentDocumentShare> findByDocumentId(Long documentId);

    List<DepartmentDocumentShare> findSharedWithUserDepartments(String userId);
}
