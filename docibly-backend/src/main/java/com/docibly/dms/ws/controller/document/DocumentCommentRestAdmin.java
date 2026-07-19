package com.docibly.dms.ws.controller.document;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import com.docibly.dms.bean.core.document.DocumentComment;
import com.docibly.dms.dao.criteria.core.document.DocumentCommentCriteria;
import com.docibly.dms.service.facade.document.DocumentCommentService;
import com.docibly.dms.ws.converter.document.DocumentCommentConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.document.request.CreateDocumentCommentRequest;
import com.docibly.dms.ws.dto.document.request.UpdateDocumentCommentRequest;
import com.docibly.dms.ws.dto.document.response.DocumentCommentResponse;

@RestController
@RequestMapping("/api/v1/documentcomments")
@io.swagger.v3.oas.annotations.tags.Tag(name = "DocumentComment", description = "DocumentComment management API")
public class DocumentCommentRestAdmin {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "content", "isResolved", "resolvedAt", "pageNumber", "positionX", "positionY", "isEdited", "editedAt"
    );

    private final DocumentCommentService service;
    private final DocumentCommentConverter converter;

    public DocumentCommentRestAdmin(DocumentCommentService service, DocumentCommentConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @Operation(summary = "List DocumentComment records (paginated)")
    public ResponseEntity<PageResponse<DocumentCommentResponse>> findAll(
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
        final var result = service.findPaginatedByCriteria(new DocumentCommentCriteria(), pageable)
                .map(converter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentCommentResponse> findById(@PathVariable Long id) {
        DocumentComment entity = service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DocumentComment not found with id: " + id));
        return ResponseEntity.ok(converter.toResponse(entity));
    }

    @PostMapping
    @Operation(summary = "Create a new DocumentComment")
    public ResponseEntity<DocumentCommentResponse> create(@Valid @RequestBody CreateDocumentCommentRequest request) {
        DocumentComment entity = converter.toEntity(request);
        DocumentComment created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing DocumentComment")
    public ResponseEntity<DocumentCommentResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateDocumentCommentRequest request) {
        DocumentComment entity = converter.toEntity(request);
        entity.setId(id);
        DocumentComment updated = service.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a DocumentComment")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DocumentComment not found with id: " + id));
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

