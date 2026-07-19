package com.docibly.dms.ws.controller.searchindex.user;

import com.docibly.dms.bean.core.searchindex.DocumentIndex;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class SearchController {

    private final ElasticsearchOperations elasticsearchOperations;

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam("q") String query,
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (query == null || query.isBlank()) {
            return ResponseEntity.ok(new SearchResponse(List.of(), 0, page, size));
        }

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(qb -> qb
                        .bool(b -> b
                                .must(m -> m
                                        .multiMatch(mm -> mm
                                                .query(query)
                                                .fields("title^3", "content", "tags")
                                        )
                                )
                                .filter(f -> f
                                        .term(t -> t
                                                .field("organizationId")
                                                .value(orgId)
                                        )
                                )
                        )
                )
                .withPageable(PageRequest.of(page, size))
                .build();

        SearchHits<DocumentIndex> searchHits = elasticsearchOperations.search(nativeQuery, DocumentIndex.class);

        List<DocumentIndex> results = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();

        return ResponseEntity.ok(new SearchResponse(
                results,
                searchHits.getTotalHits(),
                page,
                size
        ));
    }

    public record SearchResponse(
            List<DocumentIndex> results,
            long totalHits,
            int page,
            int size
    ) {}
}
