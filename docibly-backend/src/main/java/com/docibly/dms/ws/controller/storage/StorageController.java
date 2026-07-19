package com.docibly.dms.ws.controller.storage;

import com.docibly.dms.service.facade.storage.StorageService;
import com.docibly.dms.service.facade.storage.StorageService.PresignedPutResult;
import com.docibly.dms.ws.dto.storage.FileMetadata;
import com.docibly.dms.ws.dto.storage.PresignedUrlRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;

@RestController
@RequestMapping("/api/v1/storage")
@ConditionalOnBean(StorageService.class)
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@Tag(name = "Storage", description = "File upload and download via MinIO")
@SecurityRequirement(name = "bearerAuth")
public class StorageController {

    private final StorageService storageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file through the backend (≤ configured max size)")
    public ResponseEntity<FileMetadata> upload(
            @RequestPart("file") final MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "uploads") final String folder) {
        return ResponseEntity.ok(this.storageService.upload(file, folder));
    }

    @PostMapping("/presign/put")
    @Operation(summary = "Request a presigned PUT URL for direct browser-to-MinIO upload")
    public ResponseEntity<PresignedPutResult> presignPut(
            @Valid @RequestBody final PresignedUrlRequest request) {
        final Duration expiry = Duration.ofHours(1);
        final PresignedPutResult result = this.storageService.presignPut(
                request.getFileName(),
                request.getContentType(),
                request.getFolder(),
                expiry
        );
        return ResponseEntity.ok(result);
    }

    @GetMapping("/presign/get")
    @Operation(summary = "Get a presigned GET URL to download or display a stored file")
    public ResponseEntity<String> presignGet(
            @RequestParam final String objectKey,
            @RequestParam(defaultValue = "3600") final long expirySeconds) {
        final String url = this.storageService.presignGet(objectKey, Duration.ofSeconds(expirySeconds));
        return ResponseEntity.ok(url);
    }

    @GetMapping("/download")
    @Operation(summary = "Stream a file directly through the backend (use presigned GET for large files)")
    public ResponseEntity<byte[]> download(@RequestParam final String objectKey) throws Exception {
        try (InputStream stream = this.storageService.download(objectKey)) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectKey + "\"")
                    .body(stream.readAllBytes());
        }
    }

    @DeleteMapping
    @Operation(summary = "Permanently delete a stored object")
    public ResponseEntity<Void> delete(@RequestParam final String objectKey) {
        this.storageService.delete(objectKey);
        return ResponseEntity.noContent().build();
    }
}
