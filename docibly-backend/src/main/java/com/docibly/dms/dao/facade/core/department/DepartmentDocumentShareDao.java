package com.docibly.dms.dao.facade.core.department;

import com.docibly.dms.bean.core.department.DepartmentDocumentShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentDocumentShareDao extends JpaRepository<DepartmentDocumentShare, Long>, JpaSpecificationExecutor<DepartmentDocumentShare> {

    List<DepartmentDocumentShare> findByDepartment_Id(Long departmentId);

    List<DepartmentDocumentShare> findByDepartment_Organization_Id(Long organizationId);

    List<DepartmentDocumentShare> findByDocument_Id(Long documentId);

    DepartmentDocumentShare findByRef(String ref);
    int deleteByRef(String ref);
}
