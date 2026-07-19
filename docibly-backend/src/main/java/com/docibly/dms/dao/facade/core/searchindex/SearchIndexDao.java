package com.docibly.dms.dao.facade.core.searchindex;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.docibly.dms.bean.core.searchindex.SearchIndex;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SearchIndexDao extends JpaRepository<SearchIndex, Long>, JpaSpecificationExecutor<SearchIndex> {

    SearchIndex findByRef(String ref);
    int deleteByRef(String ref);

}

