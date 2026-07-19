package com.docibly.dms.ws.controller.organization.admin;

import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.dao.criteria.core.organization.OrganizationCriteria;
import com.docibly.dms.service.facade.organization.OrganizationService;
import com.docibly.dms.ws.converter.organization.admin.OrganizationAdminConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.organization.admin.request.CreateOrganizationRequest;
import com.docibly.dms.ws.dto.organization.admin.request.UpdateOrganizationRequest;
import com.docibly.dms.ws.dto.organization.admin.response.OrganizationAdminDto;
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
 * REST Controller for Organization - Admin endpoints.
 * Access: ADMIN
 */
@RestController
@RequestMapping("/api/v1/admin/organizations")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class OrganizationAdminRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "name", "slug", "description", "logoUrl", "website", "storageUsedBytes", "storageQuotaBytes", "maxMembers", "isActive", "billingEmail", "planTier", "trialEndsAt"
    );

    private final OrganizationService organizationService;
    private final OrganizationAdminConverter converter;

    @PostMapping
    public ResponseEntity<OrganizationAdminDto> create(@Valid @RequestBody CreateOrganizationRequest request) {
        Organization entity = converter.toEntity(request);
        Organization saved = organizationService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationAdminDto> findById(@PathVariable Long id) {
        Organization entity = organizationService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Organization not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrganizationAdminDto>> findAll(
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
        final var result = organizationService.findPaginatedByCriteria(new OrganizationCriteria(), pageable)
                .map(converter::toDto);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationAdminDto> update(@PathVariable Long id, @Valid @RequestBody UpdateOrganizationRequest request) {
        Organization entity = converter.toEntity(request);
        entity.setId(id);
        Organization updated = organizationService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
