package com.docibly.dms.ws.controller.document.user;

import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.dao.criteria.core.document.DocumentVersionCriteria;
import com.docibly.dms.service.facade.document.DocumentVersionService;
import com.docibly.dms.ws.converter.document.user.DocumentVersionUserConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.document.user.request.CreateDocumentVersionRequest;
import com.docibly.dms.ws.dto.document.user.request.UpdateDocumentVersionRequest;
import com.docibly.dms.ws.dto.document.user.response.DocumentVersionUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Set;

/**
 * REST Controller for DocumentVersion - User endpoints.
 * Access: USER
 */
@RestController
@RequestMapping("/api/v1/user/documentversions")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class DocumentVersionUserRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "versionNumber", "label", "changeNote", "originalFilename", "storedFilename", "storageKey", "fileSizeBytes", "mimeType", "checksum", "isCurrentVersion", "ocrStatus", "ocrText"
    );

    private final DocumentVersionService documentVersionService;
    private final DocumentVersionUserConverter converter;

    @PostMapping
    public ResponseEntity<DocumentVersionUserDto> create(@Valid @RequestBody CreateDocumentVersionRequest request) {
        DocumentVersion entity = converter.toEntity(request);
        DocumentVersion saved = documentVersionService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @PostMapping(value = "/reupload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentVersionUserDto> reupload(
            @RequestParam("documentId") Long documentId,
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam(value = "label", required = false) String label,
            @RequestParam(value = "changeNote", required = false) String changeNote) {
        DocumentVersion saved = documentVersionService.reupload(documentId, orgId, file, label, changeNote);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentVersionUserDto> findById(@PathVariable Long id) {
        DocumentVersion entity = documentVersionService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DocumentVersion not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DocumentVersionUserDto>> findAll(
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
    public ResponseEntity<DocumentVersionUserDto> update(@PathVariable Long id, @Valid @RequestBody UpdateDocumentVersionRequest request) {
        DocumentVersion entity = converter.toEntity(request);
        entity.setId(id);
        DocumentVersion updated = documentVersionService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @GetMapping("/by-document/{documentId}")
    public ResponseEntity<List<DocumentVersionUserDto>> findByDocumentId(@PathVariable Long documentId) {
        List<DocumentVersion> versions = documentVersionService.findByDocumentId(documentId);
        return ResponseEntity.ok(versions.stream().map(converter::toDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentVersionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
