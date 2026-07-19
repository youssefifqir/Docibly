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

    Optional<SearchIndex> findByDocumentId(Long documentId);
    void deleteByDocumentId(Long documentId);

    // websearch_to_tsquery tolerates arbitrary free-text input (quotes, "-word" exclusion,
    // "or") without throwing, unlike to_tsquery which demands exact tsquery operator syntax —
    // matches what users type into a search box. search_vector is a precomputed/indexed
    // (GIN) generated column, so this doesn't recompute to_tsvector per row per query.
    @Query(value = """
            SELECT * FROM app_searchindex
            WHERE deleted_at IS NULL
              AND organization_id = :orgId
              AND search_vector @@ websearch_to_tsquery('english', :query)
            ORDER BY ts_rank(search_vector, websearch_to_tsquery('english', :query)) DESC
            """,
            countQuery = """
            SELECT count(*) FROM app_searchindex
            WHERE deleted_at IS NULL
              AND organization_id = :orgId
              AND search_vector @@ websearch_to_tsquery('english', :query)
            """,
            nativeQuery = true)
    Page<SearchIndex> searchFullText(@Param("orgId") Long orgId, @Param("query") String query, Pageable pageable);

}

