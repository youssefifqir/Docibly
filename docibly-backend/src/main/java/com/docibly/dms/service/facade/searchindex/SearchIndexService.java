package com.docibly.dms.service.facade.searchindex;

import com.docibly.dms.bean.core.searchindex.SearchIndex;
import com.docibly.dms.dao.criteria.core.searchindex.SearchIndexCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface SearchIndexService {

    SearchIndex create(SearchIndex t);
    SearchIndex update(SearchIndex t);
    List<SearchIndex> update(List<SearchIndex> ts, boolean createIfNotExist);
    Optional<SearchIndex> findById(Long id);
    SearchIndex save(SearchIndex entity);
    void deleteById(Long id);
    Optional<SearchIndex> findAndDeleteById(Long id);
    SearchIndex findOrSave(SearchIndex t);
    SearchIndex findByReferenceEntity(SearchIndex t);
    SearchIndex findWithAssociatedLists(Long id);
    List<SearchIndex> findAll();
    List<SearchIndex> findByCriteria(SearchIndexCriteria criteria);
    Page<SearchIndex> findPaginatedByCriteria(SearchIndexCriteria criteria, Pageable pageable);
    int getDataSize(SearchIndexCriteria criteria);
    List<SearchIndex> delete(List<SearchIndex> ts);
    SearchIndex findByRef(String ref);
}

