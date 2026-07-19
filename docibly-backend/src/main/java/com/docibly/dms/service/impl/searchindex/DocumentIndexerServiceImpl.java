package com.docibly.dms.service.impl.searchindex;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.searchindex.SearchIndex;
import com.docibly.dms.dao.facade.core.searchindex.SearchIndexDao;
import com.docibly.dms.service.facade.searchindex.DocumentIndexerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentIndexerServiceImpl implements DocumentIndexerService {

    private final SearchIndexDao searchIndexDao;

    @Override
    public void indexDocument(Document document) {
        try {
            String tagsStr = "";
            if (document.getTags() != null && !document.getTags().isEmpty()) {
                tagsStr = document.getTags().stream()
                        .map(t -> t.getName())
                        .collect(Collectors.joining(" "));
            }

            SearchIndex entity = searchIndexDao.findByDocumentId(document.getId())
                    .orElseGet(() -> SearchIndex.builder().documentId(document.getId()).build());

            entity.setDocumentTitle(document.getTitle());
            entity.setFullText(document.getDescription());
            entity.setOcrText(document.getOcrText());
            entity.setTags(tagsStr);
            entity.setMimeType(document.getMimeType());
            entity.setOrganizationId(document.getOrganization() != null
                    ? document.getOrganization().getId() : null);
            entity.setVisibility(document.getVisibility());
            entity.setOwnerId(document.getCreatedBy());
            entity.setIndexedAt(LocalDateTime.now());

            searchIndexDao.save(entity);
            log.debug("Indexed document {} for full-text search", document.getId());
        } catch (Exception e) {
            log.error("Failed to index document {} for full-text search: {}", document.getId(), e.getMessage());
        }
    }

    @Override
    public void removeDocument(Long documentId) {
        try {
            searchIndexDao.deleteByDocumentId(documentId);
            log.debug("Removed document {} from search index", documentId);
        } catch (Exception e) {
            log.error("Failed to remove document {} from search index: {}", documentId, e.getMessage());
        }
    }
}
