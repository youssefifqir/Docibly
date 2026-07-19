package com.docibly.dms.config.storage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.storage.minio")
public class StorageProperties {

    private boolean enabled;

    @NotBlank
    private String endpoint;

    @NotBlank
    private String accessKey;

    @NotBlank
    private String secretKey;

    @NotBlank
    private String bucket;

    @Min(60)
    private long presignedUrlExpirySeconds = 3600L;

    @NotEmpty
    private List<String> allowedContentTypes = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/pdf", "text/plain"
    );

    @Min(1)
    private long maxFileSizeBytes = 10_485_760L; // 10 MB

    public boolean isContentTypeAllowed(final String contentType) {
        if (contentType == null) return false;
        return this.allowedContentTypes.stream()
                .anyMatch(allowed -> allowed.equalsIgnoreCase(contentType.split(";")[0].trim()));
    }
}
