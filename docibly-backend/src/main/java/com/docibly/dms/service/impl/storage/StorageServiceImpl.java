package com.docibly.dms.service.impl.storage;

import com.docibly.dms.config.storage.StorageProperties;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.storage.StorageService;
import com.docibly.dms.ws.dto.storage.FileMetadata;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@Service
@ConditionalOnBean(MinioClient.class)
@RequiredArgsConstructor
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final MinioClient minioClient;
    private final StorageProperties storageProperties;

    @Override
    public FileMetadata upload(final MultipartFile file, final String folder) {
        validateFile(file);

        final String objectKey = buildObjectKey(folder, file.getOriginalFilename());
        final String bucket = this.storageProperties.getBucket();

        try {
            this.minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.debug("Uploaded '{}' as '{}' ({} bytes)", file.getOriginalFilename(), objectKey, file.getSize());
        } catch (final Exception e) {
            log.error("MinIO upload failed for object '{}': {}", objectKey, e.getMessage());
            throw new BusinessException(ErrorCode.STORAGE_UPLOAD_FAILED);
        }

        final String presignedUrl = presignGet(objectKey,
                Duration.ofSeconds(this.storageProperties.getPresignedUrlExpirySeconds()));

        return new FileMetadata(objectKey, sanitizeName(file.getOriginalFilename()),
                file.getContentType(), file.getSize(), presignedUrl);
    }

    @Override
    public FileMetadata upload(final InputStream data, final String objectKey,
                                final String contentType, final long contentLength) {
        final String bucket = this.storageProperties.getBucket();
        try {
            this.minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(data, contentLength, -1)
                            .contentType(contentType)
                            .build()
            );
            log.debug("Uploaded raw data as '{}' ({} bytes)", objectKey, contentLength);
        } catch (final Exception e) {
            log.error("MinIO upload failed for object '{}': {}", objectKey, e.getMessage());
            throw new BusinessException(ErrorCode.STORAGE_UPLOAD_FAILED);
        }
        return new FileMetadata(objectKey, objectKey, contentType, contentLength, null);
    }

    @Override
    public PresignedPutResult presignPut(final String fileName, final String contentType,
                                          final String folder, final Duration expiry) {
        if (!this.storageProperties.isContentTypeAllowed(contentType)) {
            throw new BusinessException(ErrorCode.STORAGE_CONTENT_TYPE_NOT_ALLOWED);
        }
        final String objectKey = buildObjectKey(folder, fileName);
        try {
            final String url = this.minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(this.storageProperties.getBucket())
                            .object(objectKey)
                            .method(Method.PUT)
                            .expiry((int) expiry.getSeconds())
                            .build()
            );
            log.debug("Presigned PUT URL created for object '{}'", objectKey);
            return new PresignedPutResult(objectKey, url);
        } catch (final Exception e) {
            log.error("Failed to generate presigned PUT URL for '{}': {}", objectKey, e.getMessage());
            throw new BusinessException(ErrorCode.STORAGE_PRESIGN_FAILED);
        }
    }

    @Override
    public String presignGet(final String objectKey, final Duration expiry) {
        try {
            return this.minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(this.storageProperties.getBucket())
                            .object(objectKey)
                            .method(Method.GET)
                            .expiry((int) expiry.getSeconds())
                            .build()
            );
        } catch (final Exception e) {
            log.error("Failed to generate presigned GET URL for '{}': {}", objectKey, e.getMessage());
            throw new BusinessException(ErrorCode.STORAGE_PRESIGN_FAILED);
        }
    }

    @Override
    public void delete(final String objectKey) {
        try {
            this.minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(this.storageProperties.getBucket())
                            .object(objectKey)
                            .build()
            );
            log.debug("Deleted object '{}'", objectKey);
        } catch (final Exception e) {
            log.error("Failed to delete object '{}': {}", objectKey, e.getMessage());
            throw new BusinessException(ErrorCode.STORAGE_DELETE_FAILED);
        }
    }

    @Override
    public InputStream download(final String objectKey) {
        try {
            return this.minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(this.storageProperties.getBucket())
                            .object(objectKey)
                            .build()
            );
        } catch (final Exception e) {
            log.error("Failed to download object '{}': {}", objectKey, e.getMessage());
            throw new BusinessException(ErrorCode.STORAGE_DOWNLOAD_FAILED);
        }
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private void validateFile(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.STORAGE_FILE_EMPTY);
        }
        if (file.getSize() > this.storageProperties.getMaxFileSizeBytes()) {
            throw new BusinessException(ErrorCode.STORAGE_FILE_TOO_LARGE);
        }
        if (!this.storageProperties.isContentTypeAllowed(file.getContentType())) {
            throw new BusinessException(ErrorCode.STORAGE_CONTENT_TYPE_NOT_ALLOWED);
        }
    }

    /**
     * Builds a safe, collision-free object key.
     * Format: {@code {folder}/{uuid}.{extension}}
     * Using UUID prevents path traversal and filename collisions.
     */
    private String buildObjectKey(final String folder, final String originalFilename) {
        final String extension = extractExtension(originalFilename);
        final String uuid = UUID.randomUUID().toString();
        final String base = (folder != null && !folder.isBlank())
                ? sanitizeName(folder) + "/" + uuid
                : uuid;
        return extension.isBlank() ? base : base + "." + extension;
    }

    /**
     * Extracts the lowercase file extension, returning empty string if none.
     */
    private String extractExtension(final String filename) {
        if (filename == null || !filename.contains(".")) return "";
        final String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        // Allow only simple alphanumeric extensions to prevent injection
        return ext.matches("[a-z0-9]{1,10}") ? ext : "";
    }

    /**
     * Strips directory separators and dangerous characters from a name segment.
     */
    private String sanitizeName(final String name) {
        if (name == null) return "uploads";
        return name.replaceAll("[^a-zA-Z0-9._-]", "_").replaceAll("\\.{2,}", "_");
    }
}
