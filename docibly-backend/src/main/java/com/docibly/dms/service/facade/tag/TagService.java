package com.docibly.dms.service.facade.tag;

import com.docibly.dms.bean.core.tag.Tag;
import com.docibly.dms.dao.criteria.core.tag.TagCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface TagService {

    Tag create(Tag t);
    Tag update(Tag t);
    List<Tag> update(List<Tag> ts, boolean createIfNotExist);
    Optional<Tag> findById(Long id);
    Tag save(Tag entity);
    void deleteById(Long id);
    Optional<Tag> findAndDeleteById(Long id);
    Tag findOrSave(Tag t);
    Tag findByReferenceEntity(Tag t);
    Tag findWithAssociatedLists(Long id);
    List<Tag> findAll();
    List<Tag> findByCriteria(TagCriteria criteria);
    Page<Tag> findPaginatedByCriteria(TagCriteria criteria, Pageable pageable);
    int getDataSize(TagCriteria criteria);
    List<Tag> delete(List<Tag> ts);
    Tag findByRef(String ref);
}

