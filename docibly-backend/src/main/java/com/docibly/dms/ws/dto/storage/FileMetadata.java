package com.docibly.dms.ws.dto.storage;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Metadata returned after a successful file upload")
public record FileMetadata(

        @Schema(description = "Unique object key used to reference the file in storage")
        String objectKey,

        @Schema(description = "Original filename provided by the client (sanitized)")
        String originalName,

        @Schema(description = "MIME type of the uploaded file")
        String contentType,

        @Schema(description = "File size in bytes")
        long size,

        @Schema(description = "Short-lived presigned URL for immediate download/preview (null when not requested)")
        String presignedUrl
) {}
