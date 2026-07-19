package com.docibly.dms.ws.controller.organization.user;

import com.docibly.dms.bean.core.organization.OrganizationRequest;
import com.docibly.dms.dao.criteria.core.organization.OrganizationRequestCriteria;
import com.docibly.dms.service.facade.organization.OrganizationRequestService;
import com.docibly.dms.ws.converter.organization.user.OrganizationRequestUserConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.organization.user.request.CreateOrganizationRequestRequest;
import com.docibly.dms.ws.dto.organization.user.request.UpdateOrganizationRequestRequest;
import com.docibly.dms.ws.dto.organization.user.response.OrganizationRequestUserDto;
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
 * REST Controller for OrganizationRequest - User endpoints.
 * Access: USER
 */
@RestController
@RequestMapping("/api/v1/user/organizationrequests")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class OrganizationRequestUserRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "requestedName", "requestedSlug", "description", "intendedUse", "status", "reviewedAt", "rejectionReason", "createdOrganizationId"
    );

    private final OrganizationRequestService organizationRequestService;
    private final OrganizationRequestUserConverter converter;

    @PostMapping
    public ResponseEntity<OrganizationRequestUserDto> create(@Valid @RequestBody CreateOrganizationRequestRequest request) {
        OrganizationRequest entity = converter.toEntity(request);
        OrganizationRequest saved = organizationRequestService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationRequestUserDto> findById(@PathVariable Long id) {
        OrganizationRequest entity = organizationRequestService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrganizationRequest not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrganizationRequestUserDto>> findAll(
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
        final var result = organizationRequestService.findPaginatedByCriteria(new OrganizationRequestCriteria(), pageable)
                .map(converter::toDto);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationRequestUserDto> update(@PathVariable Long id, @Valid @RequestBody UpdateOrganizationRequestRequest request) {
        OrganizationRequest entity = converter.toEntity(request);
        entity.setId(id);
        OrganizationRequest updated = organizationRequestService.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizationRequestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
