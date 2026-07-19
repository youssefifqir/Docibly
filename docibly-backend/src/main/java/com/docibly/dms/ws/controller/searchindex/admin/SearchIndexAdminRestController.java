package com.docibly.dms.ws.controller.searchindex.admin;

import com.docibly.dms.bean.core.searchindex.SearchIndex;
import com.docibly.dms.dao.criteria.core.searchindex.SearchIndexCriteria;
import com.docibly.dms.service.facade.searchindex.SearchIndexService;
import com.docibly.dms.ws.converter.searchindex.admin.SearchIndexAdminConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.searchindex.admin.request.CreateSearchIndexRequest;
import com.docibly.dms.ws.dto.searchindex.admin.request.UpdateSearchIndexRequest;
import com.docibly.dms.ws.dto.searchindex.admin.response.SearchIndexAdminDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.persistence.EntityNotFoundException;

import java.util.Set;

/**
 * REST Controller for SearchIndex - Admin endpoints.
 * Access: ADMIN
 */
@RestController
@RequestMapping("/api/v1/admin/searchindexs")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class SearchIndexAdminRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "documentId", "documentTitle", "fullText", "ocrText", "tags", "mimeType", "organizationId", "ownerId", "visibility", "indexedAt"
    );

    private final SearchIndexService searchIndexService;
    private final SearchIndexAdminConverter converter;

    @PostMapping
    public ResponseEntity<SearchIndexAdminDto> create(@Valid @RequestBody CreateSearchIndexRequest request) {
        SearchIndex entity = converter.toEntity(request);
        SearchIndex saved = searchIndexService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SearchIndexAdminDto> findById(@PathVariable Long id) {
        SearchIndex entity = searchIndexService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SearchIndex not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<SearchIndexAdminDto>> findAll(
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") @Max(200) final int size,
            @RequestParam(defaultValue = "createdDate") final String sortBy,
            @RequestParam(defaultValue = "desc") final String sortDir) {
        if (!ALLOWED_SORT_COLUMNS.contains(sortBy)) {
            return ResponseEntity.badRequest().body(null);
        }
        if (!"asc".equalsIgnoreCase(sortDir) && !"desc".equalsIgnoreCase(sortDir)) {
            return ResponseEntity.badRequest().body(null);
        }
        final int effectiveSize = Math.min(size, 200);
        final var pageable = PageRequest.of(page, effectiveSize,
                "asc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        final var result = searchIndexService.findPaginatedByCriteria(new SearchIndexCriteria(), pageable)
                .map(converter::toDto);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SearchIndexAdminDto> update(@PathVariable Long id, @Valid @RequestBody UpdateSearchIndexRequest request) {
        SearchIndex entity = converter.toEntity(request);
        entity.setId(id);
        SearchIndex updated = searchIndexService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        searchIndexService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
