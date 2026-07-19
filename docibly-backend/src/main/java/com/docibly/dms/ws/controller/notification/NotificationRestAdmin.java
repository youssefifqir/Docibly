package com.docibly.dms.ws.controller.notification;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import com.docibly.dms.bean.core.notification.Notification;
import com.docibly.dms.dao.criteria.core.notification.NotificationCriteria;
import com.docibly.dms.service.facade.notification.NotificationService;
import com.docibly.dms.ws.converter.notification.NotificationConverter;
import com.docibly.dms.ws.dto.PageResponse;
import com.docibly.dms.ws.dto.notification.request.CreateNotificationRequest;
import com.docibly.dms.ws.dto.notification.request.UpdateNotificationRequest;
import com.docibly.dms.ws.dto.notification.response.NotificationResponse;

@RestController
@RequestMapping("/api/v1/notifications")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Notification", description = "Notification management API")
public class NotificationRestAdmin {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of(
        "id", "ref", "createdDate", "lastModifiedDate", "type", "title", "message", "read", "readAt", "targetUrl", "relatedEntityType", "relatedEntityId"
    );

    private final NotificationService service;
    private final NotificationConverter converter;

    public NotificationRestAdmin(NotificationService service, NotificationConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @Operation(summary = "List Notification records (paginated)")
    public ResponseEntity<PageResponse<NotificationResponse>> findAll(
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
        final var result = service.findPaginatedByCriteria(new NotificationCriteria(), pageable)
                .map(converter::toResponse);
        return ResponseEntity.ok(PageResponse.from(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> findById(@PathVariable Long id) {
        Notification entity = service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
        return ResponseEntity.ok(converter.toResponse(entity));
    }

    @PostMapping
    @Operation(summary = "Create a new Notification")
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody CreateNotificationRequest request) {
        Notification entity = converter.toEntity(request);
        Notification created = service.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(converter.toResponse(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing Notification")
    public ResponseEntity<NotificationResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateNotificationRequest request) {
        Notification entity = converter.toEntity(request);
        entity.setId(id);
        Notification updated = service.update(entity);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(converter.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Notification")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

