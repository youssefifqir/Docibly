package com.docibly.dms.ws.controller.organization.user;

import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.dao.criteria.core.organization.OrganizationMemberCriteria;
import com.docibly.dms.service.facade.organization.OrganizationMemberService;
import com.docibly.dms.ws.converter.organization.user.OrganizationMemberUserConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.organization.user.request.CreateOrganizationMemberRequest;
import com.docibly.dms.ws.dto.organization.user.request.UpdateOrganizationMemberRequest;
import com.docibly.dms.ws.dto.organization.user.response.OrganizationMemberUserDto;
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
 * REST Controller for OrganizationMember - User endpoints.
 * Access: USER
 */
@RestController
@RequestMapping("/api/v1/user/organizationmembers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class OrganizationMemberUserRestController {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "memberRole", "joinedAt", "isActive", "invitedByEmail"
    );

    private final OrganizationMemberService organizationMemberService;
    private final OrganizationMemberUserConverter converter;

    @PostMapping
    public ResponseEntity<OrganizationMemberUserDto> create(@Valid @RequestBody CreateOrganizationMemberRequest request) {
        OrganizationMember entity = converter.toEntity(request);
        OrganizationMember saved = organizationMemberService.save(entity);
        return new ResponseEntity<>(converter.toDto(saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationMemberUserDto> findById(@PathVariable Long id) {
        OrganizationMember entity = organizationMemberService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrganizationMember not found with id: " + id));
        return ResponseEntity.ok(converter.toDto(entity));
    }

    @GetMapping
    public ResponseEntity<PageResponse<OrganizationMemberUserDto>> findAll(
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
    public ResponseEntity<OrganizationMemberUserDto> update(@PathVariable Long id, @Valid @RequestBody UpdateOrganizationMemberRequest request) {
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
