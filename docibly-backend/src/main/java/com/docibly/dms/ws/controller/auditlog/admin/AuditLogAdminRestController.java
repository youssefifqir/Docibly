package com.docibly.dms.ws.controller.auditlog.admin;

import com.docibly.dms.bean.core.auditlog.AuditLog;
import com.docibly.dms.dao.criteria.core.auditlog.AuditLogCriteria;
import com.docibly.dms.service.facade.auditlog.AuditLogService;
import com.docibly.dms.ws.converter.auditlog.admin.AuditLogAdminConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.auditlog.admin.request.CreateAuditLogRequest;
import com.docibly.dms.ws.dto.auditlog.admin.request.UpdateAuditLogRequest;
import com.docibly.dms.ws.dto.auditlog.admin.response.AuditLogAdminDto;
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
 * REST Controller for AuditLog - Admin endpoints.
 * Access: ADMIN
 */
@RestController
@RequestMapping("/api/v1/admin/auditlogs")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class AuditLogAdminRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "action", "actorUserId", "actorEmail", "targetEntityType", "targetEntityId", "organizationId", "metadata", "ipAddress", "userAgent"
    );

    private final AuditLogService auditLogService;
    private final AuditLogAdminConverter converter;

    @PostMapping
    public ResponseEntity<AuditLogAdminDto> create(@Valid @RequestBody CreateAuditLogRequest request) {
        AuditLog entity = converter.toEntity(request);
        AuditLog saved = auditLogService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogAdminDto> findById(@PathVariable Long id) {
        AuditLog entity = auditLogService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AuditLog not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<AuditLogAdminDto>> findAll(
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
        final var result = auditLogService.findPaginatedByCriteria(new AuditLogCriteria(), pageable)
                .map(converter::toDto);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditLogAdminDto> update(@PathVariable Long id, @Valid @RequestBody UpdateAuditLogRequest request) {
        AuditLog entity = converter.toEntity(request);
        entity.setId(id);
        AuditLog updated = auditLogService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        auditLogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
