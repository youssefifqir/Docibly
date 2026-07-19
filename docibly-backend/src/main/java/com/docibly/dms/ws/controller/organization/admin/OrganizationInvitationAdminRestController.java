package com.docibly.dms.ws.controller.organization.admin;

import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.dao.criteria.core.organization.OrganizationInvitationCriteria;
import com.docibly.dms.service.facade.organization.OrganizationInvitationService;
import com.docibly.dms.ws.converter.organization.admin.OrganizationInvitationAdminConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.organization.admin.request.CreateOrganizationInvitationRequest;
import com.docibly.dms.ws.dto.organization.admin.request.UpdateOrganizationInvitationRequest;
import com.docibly.dms.ws.dto.organization.admin.response.OrganizationInvitationAdminDto;
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
 * REST Controller for OrganizationInvitation - Admin endpoints.
 * Access: ADMIN
 */
@RestController
@RequestMapping("/api/v1/admin/organizationinvitations")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class OrganizationInvitationAdminRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "inviteeEmail", "intendedRole", "status", "token", "expiresAt", "acceptedAt"
    );

    private final OrganizationInvitationService organizationInvitationService;
    private final OrganizationInvitationAdminConverter converter;

    @PostMapping
    public ResponseEntity<OrganizationInvitationAdminDto> create(@Valid @RequestBody CreateOrganizationInvitationRequest request) {
        OrganizationInvitation entity = converter.toEntity(request);
        OrganizationInvitation saved = organizationInvitationService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationInvitationAdminDto> findById(@PathVariable Long id) {
        OrganizationInvitation entity = organizationInvitationService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrganizationInvitation not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrganizationInvitationAdminDto>> findAll(
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
        final var result = organizationInvitationService.findPaginatedByCriteria(new OrganizationInvitationCriteria(), pageable)
                .map(converter::toDto);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationInvitationAdminDto> update(@PathVariable Long id, @Valid @RequestBody UpdateOrganizationInvitationRequest request) {
        OrganizationInvitation entity = converter.toEntity(request);
        entity.setId(id);
        OrganizationInvitation updated = organizationInvitationService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizationInvitationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
