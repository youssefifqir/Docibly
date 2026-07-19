package com.docibly.dms.ws.controller.document.user;

import com.docibly.dms.bean.core.document.DocumentShare;
import com.docibly.dms.bean.core.enums.SharePermission;
import com.docibly.dms.dao.criteria.core.document.DocumentShareCriteria;
import com.docibly.dms.service.facade.department.DepartmentPermissionService;
import com.docibly.dms.service.facade.document.DocumentShareService;
import com.docibly.dms.ws.converter.document.user.DocumentShareUserConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.document.user.request.CreateDocumentShareRequest;
import com.docibly.dms.ws.dto.document.user.request.UpdateDocumentShareRequest;
import com.docibly.dms.ws.dto.document.user.response.DocumentShareUserDto;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;

/**
 * REST Controller for DocumentShare - User endpoints.
 * Access: USER
 */
@RestController
@RequestMapping("/api/v1/user/documentshares")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class DocumentShareUserRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "shareToken", "permission", "sharedWithEmail", "isPublicLink", "expiresAt", "isRevoked", "revokedAt", "accessCount", "lastAccessedAt", "requiresPassword", "passwordHash"
    );

    private final DocumentShareService documentShareService;
    private final DocumentShareUserConverter converter;
    private final DepartmentPermissionService permissionService;

    @PostMapping
    public ResponseEntity<DocumentShareUserDto> create(@Valid @RequestBody CreateDocumentShareRequest request) {
        DocumentShare entity = converter.toEntity(request);
        DocumentShare saved = documentShareService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createShare(
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestBody Map<String, Object> body) {
        permissionService.requirePermission(orgId, "doc.share");
        Long documentId = Long.valueOf(body.get("documentId").toString());
        String permissionStr = (String) body.getOrDefault("permission", "VIEW");
        SharePermission permission = SharePermission.fromDisplayText(permissionStr);
        String expiresAtStr = (String) body.get("expiresAt");
        LocalDateTime expiresAt = expiresAtStr != null
                ? LocalDateTime.ofInstant(Instant.parse(expiresAtStr), ZoneOffset.UTC)
                : null;
        String password = (String) body.get("password");

        DocumentShare share = documentShareService.createShare(documentId, orgId, permission, expiresAt, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", share.getId(),
                "token", share.getShareToken(),
                "permission", share.getPermission().getDisplayText(),
                "expiresAt", share.getExpiresAt(),
                "requiresPassword", share.getRequiresPassword(),
                "url", "/api/v1/public/shares/" + share.getShareToken()
        ));
    }

    @PostMapping("/{id}/revoke")
    public ResponseEntity<Void> revokeShare(@PathVariable Long id, @RequestHeader("X-Org-Id") Long orgId) {
        documentShareService.revokeShare(id, orgId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentShareUserDto> findById(@PathVariable Long id) {
        DocumentShare entity = documentShareService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DocumentShare not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DocumentShareUserDto>> findAll(
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
    public ResponseEntity<DocumentShareUserDto> update(@PathVariable Long id, @Valid @RequestBody UpdateDocumentShareRequest request) {
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
