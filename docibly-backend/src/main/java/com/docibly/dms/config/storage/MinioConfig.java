package com.docibly.dms.config.storage;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.storage.minio.enabled", havingValue = "true")
@EnableConfigurationProperties(StorageProperties.class)
@RequiredArgsConstructor
@Slf4j
public class MinioConfig {

    private final StorageProperties storageProperties;

    @Bean
    public MinioClient minioClient() {
        final MinioClient client = MinioClient.builder()
                .endpoint(this.storageProperties.getEndpoint())
                .credentials(this.storageProperties.getAccessKey(), this.storageProperties.getSecretKey())
                .build();
        ensureBucketExists(client);
        return client;
    }

    private void ensureBucketExists(final MinioClient client) {
        final String bucket = this.storageProperties.getBucket().toLowerCase();
        try {
            final boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                log.info("MinIO bucket '{}' created successfully", bucket);
            } else {
                log.debug("MinIO bucket '{}' already exists", bucket);
            }
        } catch (final Exception e) {
            log.error("Failed to initialize MinIO bucket '{}': {}", bucket, e.getMessage());
            throw new IllegalStateException("MinIO bucket initialization failed — check endpoint and credentials", e);
        }
    }
}
