package com.docibly.dms.ws.controller.organization.admin;

import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.dao.criteria.core.organization.OrganizationMemberCriteria;
import com.docibly.dms.service.facade.organization.OrganizationMemberService;
import com.docibly.dms.ws.converter.organization.admin.OrganizationMemberAdminConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.organization.admin.request.CreateOrganizationMemberRequest;
import com.docibly.dms.ws.dto.organization.admin.request.UpdateOrganizationMemberRequest;
import com.docibly.dms.ws.dto.organization.admin.response.OrganizationMemberAdminDto;
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
 * REST Controller for OrganizationMember - Admin endpoints.
 * Access: ADMIN
 */
@RestController
@RequestMapping("/api/v1/admin/organizationmembers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN')")
public class OrganizationMemberAdminRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "memberRole", "joinedAt", "isActive", "invitedByEmail"
    );

    private final OrganizationMemberService organizationMemberService;
    private final OrganizationMemberAdminConverter converter;

    @PostMapping
    public ResponseEntity<OrganizationMemberAdminDto> create(@Valid @RequestBody CreateOrganizationMemberRequest request) {
        OrganizationMember entity = converter.toEntity(request);
        OrganizationMember saved = organizationMemberService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationMemberAdminDto> findById(@PathVariable Long id) {
        OrganizationMember entity = organizationMemberService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrganizationMember not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrganizationMemberAdminDto>> findAll(
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
        final var result = organizationMemberService.findPaginatedByCriteria(new OrganizationMemberCriteria(), pageable)
                .map(converter::toDto);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationMemberAdminDto> update(@PathVariable Long id, @Valid @RequestBody UpdateOrganizationMemberRequest request) {
        OrganizationMember entity = converter.toEntity(request);
        entity.setId(id);
        OrganizationMember updated = organizationMemberService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizationMemberService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
