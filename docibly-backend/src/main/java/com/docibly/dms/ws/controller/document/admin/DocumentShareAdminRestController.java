package com.docibly.dms.ws.controller.document.admin;

import com.docibly.dms.bean.core.document.DocumentShare;
import com.docibly.dms.dao.criteria.core.document.DocumentShareCriteria;
import com.docibly.dms.service.facade.document.DocumentShareService;
import com.docibly.dms.ws.converter.document.admin.DocumentShareAdminConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.document.admin.request.CreateDocumentShareRequest;
import com.docibly.dms.ws.dto.document.admin.request.UpdateDocumentShareRequest;
import com.docibly.dms.ws.dto.document.admin.response.DocumentShareAdminDto;
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
 * REST Controller for DocumentShare - Admin endpoints.
 * Access: ADMIN
 */
@RestController
@RequestMapping("/api/v1/admin/documentshares")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class DocumentShareAdminRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "shareToken", "permission", "sharedWithEmail", "isPublicLink", "expiresAt", "isRevoked", "revokedAt", "accessCount", "lastAccessedAt", "requiresPassword", "passwordHash"
    );

    private final DocumentShareService documentShareService;
    private final DocumentShareAdminConverter converter;

    @PostMapping
    public ResponseEntity<DocumentShareAdminDto> create(@Valid @RequestBody CreateDocumentShareRequest request) {
        DocumentShare entity = converter.toEntity(request);
        DocumentShare saved = documentShareService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentShareAdminDto> findById(@PathVariable Long id) {
        DocumentShare entity = documentShareService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DocumentShare not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DocumentShareAdminDto>> findAll(
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
        final var result = documentShareService.findPaginatedByCriteria(new DocumentShareCriteria(), pageable)
                .map(converter::toDto);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentShareAdminDto> update(@PathVariable Long id, @Valid @RequestBody UpdateDocumentShareRequest request) {
        DocumentShare entity = converter.toEntity(request);
        entity.setId(id);
        DocumentShare updated = documentShareService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentShareService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
