package com.docibly.dms.ws.controller.tag;

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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.bean.core.tag.Tag;
import com.docibly.dms.dao.criteria.core.tag.TagCriteria;
import com.docibly.dms.service.facade.tag.TagService;
import com.docibly.dms.ws.converter.tag.TagConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.tag.request.CreateTagRequest;
import com.docibly.dms.ws.dto.tag.request.UpdateTagRequest;
import com.docibly.dms.ws.dto.tag.response.TagResponse;

@RestController
@RequestMapping("/api/v1/tags")
@PreAuthorize("hasAnyRole('USER')")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag", description = "Tag management API")
public class TagRestAdmin {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "name", "slug", "color", "usageCount"
    );

    private final TagService service;
    private final TagConverter converter;

    public TagRestAdmin(TagService service, TagConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @Operation(summary = "List Tag records (paginated)")
    public ResponseEntity<PageResponse<TagResponse>> findAll(
            @RequestHeader("X-Org-Id") Long orgId,
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
        final var criteria = new TagCriteria();
        criteria.setOrganizationId(orgId);
        final var result = service.findPaginatedByCriteria(criteria, pageable)
                .map(converter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> findById(@RequestHeader("X-Org-Id") Long orgId, @PathVariable Long id) {
        Tag entity = requireInOrg(id, orgId);
        return ResponseEntity.ok(converter.toResponse(entity));
    }

    @PostMapping
    @Operation(summary = "Create a new Tag")
    public ResponseEntity<TagResponse> create(@RequestHeader("X-Org-Id") Long orgId, @Valid @RequestBody CreateTagRequest request) {
        Tag entity = converter.toEntity(request);
        if (entity.getSlug() == null || entity.getSlug().isBlank()) {
            entity.setSlug(slugify(entity.getName()));
        }
        Organization orgRef = new Organization();
        orgRef.setId(orgId);
        entity.setOrganization(orgRef);
        Tag created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Tag")
    public ResponseEntity<TagResponse> update(@RequestHeader("X-Org-Id") Long orgId, @PathVariable Long id, @Valid @RequestBody UpdateTagRequest request) {
        requireInOrg(id, orgId);
        Tag entity = converter.toEntity(request);
        entity.setId(id);
        Tag updated = service.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Tag")
    public ResponseEntity<Void> deleteById(@RequestHeader("X-Org-Id") Long orgId, @PathVariable Long id) {
        requireInOrg(id, orgId);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Tag requireInOrg(Long id, Long orgId) {
        Tag entity = service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));
        if (entity.getOrganization() == null || !entity.getOrganization().getId().equals(orgId)) {
            throw new EntityNotFoundException("Tag not found with id: " + id);
        }
        return entity;
    }

    private String slugify(String name) {
        if (name == null) return null;
        return name.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }
}

