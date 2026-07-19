package com.docibly.dms.dao.facade.core.searchindex;

import com.docibly.dms.bean.core.searchindex.DocumentIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentSearchRepository extends ElasticsearchRepository<DocumentIndex, Long> {

    List<DocumentIndex> findByOrganizationId(Long organizationId);
}
