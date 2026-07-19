package com.docibly.dms.ws.controller.document.user;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.enums.DigitizeFormat;
import com.docibly.dms.dao.criteria.core.document.DocumentCriteria;
import com.docibly.dms.service.facade.department.DepartmentPermissionService;
import com.docibly.dms.service.facade.document.DocumentService;
import com.docibly.dms.service.facade.document.DocumentStorageService;
import com.docibly.dms.ws.converter.document.user.DocumentUserConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.document.user.request.CreateDocumentRequest;
import com.docibly.dms.ws.dto.document.user.request.UpdateDocumentRequest;
import com.docibly.dms.ws.dto.document.user.response.DocumentUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user/documents")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class DocumentUserRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "title", "description", "originalFilename", "storedFilename", "mimeType", "fileSizeBytes", "storageBucket", "storageKey", "status", "visibility", "currentVersionNumber", "downloadCount", "viewCount", "isPasswordProtected", "passwordHash", "expiresAt", "archivedAt", "checksum", "ocrStatus", "ocrText", "ocrProcessedAt", "ocrLanguage", "ocrConfidenceScore"
    );

    private final DocumentService documentService;
    private final DocumentStorageService documentStorageService;
    private final DocumentUserConverter converter;
    private final DepartmentPermissionService permissionService;

    @PostMapping
    public ResponseEntity<DocumentUserDto> create(@Valid @RequestBody CreateDocumentRequest request) {
        Document entity = converter.toEntity(request);
        Document saved = documentService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentUserDto> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestParam(value = "folderId", required = false) Long folderId,
            @RequestParam(value = "tagIds", required = false) List<Long> tagIds,
            @RequestParam(value = "digitizeFormat", defaultValue = "NONE") String digitizeFormat) {
        permissionService.requirePermission(orgId, "doc.upload");
        DigitizeFormat fmt = DigitizeFormat.fromDisplayText(digitizeFormat);
        Document saved = documentStorageService.upload(orgId, file, title, description, folderId, tagIds, fmt);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentUserDto> findById(@PathVariable Long id) {
        Document entity = documentService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DocumentUserDto>> findAll(
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestParam(defaultValue = "0") final int page,
            @RequestParam(defaultValue = "20") @Max(200) final int size,
            @RequestParam(defaultValue = "createdDate") final String sortBy,
            @RequestParam(defaultValue = "desc") final String sortDir) {
        permissionService.requirePermission(orgId, "doc.view");
        if (!ALLOWED_SORT_COLUMNS.contains(sortBy)) {
            return ResponseEntity.badRequest().body(null);
        }
        if (!"asc".equalsIgnoreCase(sortDir) && !"desc".equalsIgnoreCase(sortDir)) {
            return ResponseEntity.badRequest().body(null);
        }
        final int effectiveSize = Math.min(size, 200);
        final var pageable = PageRequest.of(page, effectiveSize,
                "asc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        DocumentCriteria criteria = new DocumentCriteria();
        criteria.setOrganizationId(orgId);
        final var result = documentService.findPaginatedByCriteria(criteria, pageable)
                .map(converter::toDto);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(
            @PathVariable Long id,
            @RequestHeader(value = "X-Org-Id", required = false) Long orgId) {
        if (orgId != null) permissionService.requirePermission(orgId, "doc.download");
        Document doc = documentService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with id: " + id));
        Resource resource = documentStorageService.download(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + doc.getOriginalFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/{id}/presigned-url")
    public ResponseEntity<String> presignedUrl(
            @PathVariable Long id,
            @RequestHeader(value = "X-Org-Id", required = false) Long orgId) {
        if (orgId != null) permissionService.requirePermission(orgId, "doc.download");
        String url = documentStorageService.getPresignedUrl(id);
        return ResponseEntity.ok(url);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentUserDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDocumentRequest request,
            @RequestHeader(value = "X-Org-Id", required = false) Long orgId) {
        if (orgId != null) permissionService.requirePermission(orgId, "doc.edit");
        Document entity = converter.toEntity(request);
        entity.setId(id);
        Document updated = documentService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader(value = "X-Org-Id", required = false) Long orgId) {
        if (orgId != null) permissionService.requirePermission(orgId, "doc.delete");
        documentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
