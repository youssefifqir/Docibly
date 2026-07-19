package com.docibly.dms.service.facade.storage;

import com.docibly.dms.ws.dto.storage.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;

public interface StorageService {

    /**
     * Upload a file through the backend. The object is stored under
     * {@code {folder}/{uuid}.{extension}} in the configured bucket.
     *
     * @param file   multipart file from the HTTP request
     * @param folder logical folder prefix (e.g. "avatars", "documents")
     * @return metadata including the object key and a short-lived presigned GET URL
     */
    FileMetadata upload(MultipartFile file, String folder);

    /**
     * Upload raw bytes to a specific object key.
     *
     * @param data          input stream of the file content
     * @param objectKey     full object key (including any folder prefix)
     * @param contentType   MIME type of the content
     * @param contentLength number of bytes
     * @return metadata including the object key
     */
    FileMetadata upload(InputStream data, String objectKey, String contentType, long contentLength);

    /**
     * Generate a short-lived presigned PUT URL so the client can upload
     * directly to MinIO without routing the bytes through the backend.
     * The returned key should be stored by the caller and passed back for later access.
     *
     * @param fileName    original file name (used to derive the extension)
     * @param contentType MIME type that must match when the client PUTs the file
     * @param folder      logical folder prefix
     * @param expiry      URL validity window
     * @return presigned PUT URL + the reserved object key
     */
    PresignedPutResult presignPut(String fileName, String contentType, String folder, Duration expiry);

    /**
     * Generate a short-lived presigned GET URL for reading a stored object.
     *
     * @param objectKey object key returned at upload time
     * @param expiry    URL validity window
     * @return presigned GET URL
     */
    String presignGet(String objectKey, Duration expiry);

    /**
     * Permanently remove an object from the bucket.
     *
     * @param objectKey object key returned at upload time
     */
    void delete(String objectKey);

    /**
     * Download an object as a stream. Caller is responsible for closing the stream.
     *
     * @param objectKey object key returned at upload time
     * @return input stream of the object bytes
     */
    InputStream download(String objectKey);

    record PresignedPutResult(String objectKey, String presignedUrl) {}
}
