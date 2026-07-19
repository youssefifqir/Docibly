package com.docibly.dms.ws.controller.document.admin;

import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.dao.criteria.core.document.DocumentVersionCriteria;
import com.docibly.dms.service.facade.document.DocumentVersionService;
import com.docibly.dms.ws.converter.document.admin.DocumentVersionAdminConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.document.admin.request.CreateDocumentVersionRequest;
import com.docibly.dms.ws.dto.document.admin.request.UpdateDocumentVersionRequest;
import com.docibly.dms.ws.dto.document.admin.response.DocumentVersionAdminDto;
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
 * REST Controller for DocumentVersion - Admin endpoints.
 * Access: ADMIN
 */
@RestController
@RequestMapping("/api/v1/admin/documentversions")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class DocumentVersionAdminRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "versionNumber", "label", "changeNote", "originalFilename", "storedFilename", "storageKey", "fileSizeBytes", "mimeType", "checksum", "isCurrentVersion", "ocrStatus", "ocrText"
    );

    private final DocumentVersionService documentVersionService;
    private final DocumentVersionAdminConverter converter;

    @PostMapping
    public ResponseEntity<DocumentVersionAdminDto> create(@Valid @RequestBody CreateDocumentVersionRequest request) {
        DocumentVersion entity = converter.toEntity(request);
        DocumentVersion saved = documentVersionService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentVersionAdminDto> findById(@PathVariable Long id) {
        DocumentVersion entity = documentVersionService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DocumentVersion not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DocumentVersionAdminDto>> findAll(
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
        final var result = documentVersionService.findPaginatedByCriteria(new DocumentVersionCriteria(), pageable)
                .map(converter::toDto);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentVersionAdminDto> update(@PathVariable Long id, @Valid @RequestBody UpdateDocumentVersionRequest request) {
        DocumentVersion entity = converter.toEntity(request);
        entity.setId(id);
        DocumentVersion updated = documentVersionService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentVersionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
