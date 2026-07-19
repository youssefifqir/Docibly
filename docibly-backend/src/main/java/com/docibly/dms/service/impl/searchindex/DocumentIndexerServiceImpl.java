package com.docibly.dms.service.impl.searchindex;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.searchindex.DocumentIndex;
import com.docibly.dms.dao.facade.core.searchindex.DocumentSearchRepository;
import com.docibly.dms.service.facade.searchindex.DocumentIndexerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class DocumentIndexerServiceImpl implements DocumentIndexerService {

    private final DocumentSearchRepository searchRepository;

    @Autowired(required = false)
    public DocumentIndexerServiceImpl(DocumentSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
        if (searchRepository == null) {
            log.warn("Elasticsearch is not available — document indexing disabled");
        }
    }

    @Override
    public void indexDocument(Document document) {
        if (searchRepository == null) {
            return;
        }
        try {
            String tagsStr = "";
            if (document.getTags() != null && !document.getTags().isEmpty()) {
                tagsStr = document.getTags().stream()
                        .map(t -> t.getName())
                        .collect(Collectors.joining(" "));
            }

            String content = String.join(" ",
                    nullToEmpty(document.getTitle()),
                    nullToEmpty(document.getDescription()),
                    nullToEmpty(document.getOcrText())
            ).trim();

            DocumentIndex index = DocumentIndex.builder()
                    .documentId(document.getId())
                    .title(document.getTitle())
                    .description(document.getDescription())
                    .content(content)
                    .originalFilename(document.getOriginalFilename())
                    .mimeType(document.getMimeType())
                    .tags(tagsStr)
                    .organizationId(document.getOrganization() != null
                            ? document.getOrganization().getId() : null)
                    .visibility(document.getVisibility())
                    .createdDate(document.getCreatedDate())
                    .ownerId(document.getCreatedBy())
                    .build();

            searchRepository.save(index);
            log.debug("Indexed document {} in Elasticsearch", document.getId());
        } catch (Exception e) {
            log.error("Failed to index document {} in Elasticsearch: {}", document.getId(), e.getMessage());
        }
    }

    @Override
    public void removeDocument(Long documentId) {
        if (searchRepository == null) {
            return;
        }
        try {
            searchRepository.deleteById(documentId);
            log.debug("Removed document {} from Elasticsearch", documentId);
        } catch (Exception e) {
            log.error("Failed to remove document {} from Elasticsearch: {}", documentId, e.getMessage());
        }
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
