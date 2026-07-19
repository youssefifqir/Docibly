package com.docibly.dms.ws.controller.searchindex.user;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.searchindex.DocumentIndex;
import com.docibly.dms.bean.core.searchindex.SearchIndex;
import com.docibly.dms.dao.facade.core.document.DocumentDao;
import com.docibly.dms.dao.facade.core.searchindex.SearchIndexDao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class SearchController {

    private final SearchIndexDao searchIndexDao;
    private final DocumentDao documentDao;

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam("q") String query,
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (query == null || query.isBlank()) {
            return ResponseEntity.ok(new SearchResponse(List.of(), 0, page, size));
        }

        Page<SearchIndex> hits = searchIndexDao.searchFullText(orgId, query, PageRequest.of(page, size));

        final List<Long> documentIds = hits.getContent().stream().map(SearchIndex::getDocumentId).toList();
        final Map<Long, Document> documentsById = documentDao.findAllById(documentIds).stream()
                .collect(Collectors.toMap(Document::getId, Function.identity()));

        final List<DocumentIndex> results = hits.getContent().stream()
                .map(si -> toDocumentIndex(si, documentsById.get(si.getDocumentId())))
                .toList();

        return ResponseEntity.ok(new SearchResponse(
                results,
                hits.getTotalElements(),
                page,
                size
        ));
    }

    private DocumentIndex toDocumentIndex(final SearchIndex si, final Document doc) {
        return DocumentIndex.builder()
                .documentId(si.getDocumentId())
                .title(si.getDocumentTitle())
                .description(doc != null ? doc.getDescription() : null)
                .content(si.getFullText())
                .originalFilename(doc != null ? doc.getOriginalFilename() : null)
                .mimeType(si.getMimeType())
                .tags(si.getTags())
                .organizationId(si.getOrganizationId())
                .visibility(si.getVisibility())
                .createdDate(doc != null ? doc.getCreatedDate() : null)
                .ownerId(si.getOwnerId())
                .build();
    }

    public record SearchResponse(
            List<DocumentIndex> results,
            long totalHits,
            int page,
            int size
    ) {}
}
