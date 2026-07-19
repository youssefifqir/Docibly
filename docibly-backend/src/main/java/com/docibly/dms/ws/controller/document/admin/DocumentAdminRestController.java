package com.docibly.dms.ws.controller.document.admin;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.dao.criteria.core.document.DocumentCriteria;
import com.docibly.dms.service.facade.document.DocumentService;
import com.docibly.dms.ws.converter.document.admin.DocumentAdminConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.document.admin.request.CreateDocumentRequest;
import com.docibly.dms.ws.dto.document.admin.request.UpdateDocumentRequest;
import com.docibly.dms.ws.dto.document.admin.response.DocumentAdminDto;
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
 * REST Controller for Document - Admin endpoints.
 * Access: ADMIN
 */
@RestController
@RequestMapping("/api/v1/admin/documents")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class DocumentAdminRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "title", "description", "originalFilename", "storedFilename", "mimeType", "fileSizeBytes", "storageBucket", "storageKey", "status", "visibility", "currentVersionNumber", "downloadCount", "viewCount", "isPasswordProtected", "passwordHash", "expiresAt", "archivedAt", "checksum", "ocrStatus", "ocrText", "ocrProcessedAt", "ocrLanguage", "ocrConfidenceScore"
    );

    private final DocumentService documentService;
    private final DocumentAdminConverter converter;

    @PostMapping
    public ResponseEntity<DocumentAdminDto> create(@Valid @RequestBody CreateDocumentRequest request) {
        Document entity = converter.toEntity(request);
        Document saved = documentService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentAdminDto> findById(@PathVariable Long id) {
        Document entity = documentService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DocumentAdminDto>> findAll(
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
        final var result = documentService.findPaginatedByCriteria(new DocumentCriteria(), pageable)
                .map(converter::toDto);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentAdminDto> update(@PathVariable Long id, @Valid @RequestBody UpdateDocumentRequest request) {
        Document entity = converter.toEntity(request);
        entity.setId(id);
        Document updated = documentService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
