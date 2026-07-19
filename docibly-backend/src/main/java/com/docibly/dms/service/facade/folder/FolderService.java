package com.docibly.dms.service.facade.folder;

import com.docibly.dms.bean.core.folder.Folder;
import com.docibly.dms.dao.criteria.core.folder.FolderCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface FolderService {

    Folder create(Folder t);
    Folder update(Folder t);
    List<Folder> update(List<Folder> ts, boolean createIfNotExist);
    Optional<Folder> findById(Long id);
    Folder save(Folder entity);
    void deleteById(Long id);
    Optional<Folder> findAndDeleteById(Long id);
    Folder findOrSave(Folder t);
    Folder findByReferenceEntity(Folder t);
    Folder findWithAssociatedLists(Long id);
    List<Folder> findAll();
    List<Folder> findByCriteria(FolderCriteria criteria);
    Page<Folder> findPaginatedByCriteria(FolderCriteria criteria, Pageable pageable);
    int getDataSize(FolderCriteria criteria);
    List<Folder> delete(List<Folder> ts);
    Folder findByRef(String ref);
}

