package com.docibly.dms.service.impl.ocr;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.bean.core.enums.AuditAction;
import com.docibly.dms.bean.core.enums.DigitizeFormat;
import com.docibly.dms.bean.core.enums.DocumentStatus;
import com.docibly.dms.bean.core.enums.DocumentVisibility;
import com.docibly.dms.bean.core.enums.OcrStatus;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.dao.facade.core.document.DocumentDao;
import com.docibly.dms.dao.facade.core.document.DocumentVersionDao;
import com.docibly.dms.dao.facade.security.UserDao;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.auditlog.AuditLogger;
import com.docibly.dms.service.facade.notification.NotificationFiringService;
import com.docibly.dms.service.facade.ocr.OcrProcessingService;
import com.docibly.dms.service.facade.ocr.OcrService;
import com.docibly.dms.service.facade.searchindex.DocumentIndexerService;
import com.docibly.dms.service.impl.ocr.PdfTextExtractionService;
import com.docibly.dms.service.facade.storage.StorageService;
import com.docibly.dms.config.storage.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OcrProcessingServiceImpl implements OcrProcessingService {

    private static final Logger log = LoggerFactory.getLogger(OcrProcessingServiceImpl.class);

    private final DocumentDao documentDao;
    private final DocumentVersionDao documentVersionDao;
    private final OcrService ocrService;
    private final StorageService storageService;
    private final StorageProperties storageProperties;
    private final UserDao userDao;
    private final DocumentIndexerService documentIndexerService;
    private final NotificationFiringService notificationFiringService;
    private final AuditLogger auditLogger;
    private final PdfTextExtractionService pdfTextExtractionService;

    @Override
    @Async
    @Transactional
    public void processDocument(Long documentId, DigitizeFormat digitizeFormat) {
        Document document = documentDao.findById(documentId).orElse(null);
        if (document == null) {
            log.warn("OCR skipped: document {} not found", documentId);
            return;
        }
        if (document.getOcrStatus() == OcrStatus.COMPLETED || document.getOcrStatus() == OcrStatus.PROCESSING) {
            log.debug("OCR skipped: document {} already has status {}", documentId, document.getOcrStatus());
            return;
        }

        process(document, document.getStorageKey(), document.getMimeType(),
                document.getOriginalFilename(), document.getOcrLanguage(), digitizeFormat);
    }

    @Override
    @Async
    @Transactional
    public void processDocumentVersion(Long documentVersionId) {
        DocumentVersion version = documentVersionDao.findById(documentVersionId).orElse(null);
        if (version == null) {
            log.warn("OCR skipped: document version {} not found", documentVersionId);
            return;
        }
        if (version.getOcrStatus() == OcrStatus.COMPLETED || version.getOcrStatus() == OcrStatus.PROCESSING) {
            return;
        }

        try {
            version.setOcrStatus(OcrStatus.PROCESSING);
            documentVersionDao.save(version);

            InputStream stream = storageService.download(version.getStorageKey());
            OcrService.OcrResult result = ocrService.extractText(stream,
                    version.getOriginalFilename(), null);

            version.setOcrText(result.text());
            version.setOcrStatus(OcrStatus.COMPLETED);
            documentVersionDao.save(version);

            log.info("OCR completed for document version {} ({} chars)", documentVersionId, result.text().length());
        } catch (Exception e) {
            log.error("OCR failed for document version {}: {}", documentVersionId, e.getMessage());
            version.setOcrStatus(OcrStatus.FAILED);
            documentVersionDao.save(version);
        }
    }

    private void process(Document document, String storageKey, String mimeType,
                         String originalFilename, String ocrLanguage, DigitizeFormat digitizeFormat) {
        if (!isOcrSupported(mimeType)) {
            document.setOcrStatus(OcrStatus.SKIPPED);
            documentDao.save(document);
            log.debug("OCR skipped for document {}: unsupported type {}", document.getId(), mimeType);
            return;
        }

        // Step 1: Try direct text extraction for PDFs (no Tesseract needed)
        if ("application/pdf".equals(mimeType)) {
            try {
                InputStream stream = storageService.download(storageKey);
                PdfTextExtractionService.PdfTextResult pdfResult = pdfTextExtractionService.extractText(stream);
                if (pdfResult.hasSubstantialText()) {
                    document.setOcrStatus(OcrStatus.COMPLETED);
                    document.setOcrText(pdfResult.text());
                    document.setOcrConfidenceScore(java.math.BigDecimal.valueOf(100.0));
                    document.setOcrProcessedAt(LocalDateTime.now());
                    document.setOcrLanguage(ocrLanguage);
                    documentDao.save(document);

                    documentIndexerService.indexDocument(document);

                    log.info("PDF text extracted directly for document {} ({} chars)",
                            document.getId(), pdfResult.text().length());

                    Long orgId = document.getOrganization() != null ? document.getOrganization().getId() : null;
                    auditLogger.log(AuditAction.OCR_COMPLETED, "Document", document.getId().toString(),
                            orgId, "chars=" + pdfResult.text().length() + ", method=direct");
                    return;
                }
                log.debug("Direct PDF extraction returned little text for document {} - falling back to OCR",
                        document.getId());
            } catch (Exception e) {
                log.warn("Direct PDF extraction failed for document {}: {} - falling back to OCR",
                        document.getId(), e.getMessage());
            }
        }

        // Step 2: Fall back to Tesseract OCR (for scanned PDFs, images, etc.)
        try {
            document.setOcrStatus(OcrStatus.PROCESSING);
            documentDao.save(document);

            InputStream stream = storageService.download(storageKey);
            OcrService.OcrResult result = ocrService.extractText(stream, originalFilename, ocrLanguage, digitizeFormat);

            document.setOcrText(result.text());
            document.setOcrConfidenceScore(java.math.BigDecimal.valueOf(result.confidenceScore()));
            document.setOcrProcessedAt(LocalDateTime.now());
            document.setOcrLanguage(result.language());
            document.setOcrStatus(OcrStatus.COMPLETED);
            documentDao.save(document);

            documentIndexerService.indexDocument(document);

            if (document.getCreatedBy() != null) {
                notificationFiringService.ocrReady(
                        document.getCreatedBy(), document.getTitle(), document.getId());
            }

            if (result.outputBytes() != null && result.outputFormat() != DigitizeFormat.NONE) {
                createDigitizedVersion(document, result);
            }

            log.info("OCR completed for document {} ({} chars, confidence={})",
                    document.getId(), result.text().length(), result.confidenceScore());

            Long orgId = document.getOrganization() != null ? document.getOrganization().getId() : null;
            auditLogger.log(AuditAction.OCR_COMPLETED, "Document", document.getId().toString(),
                    orgId, "chars=" + result.text().length() + ", confidence=" + result.confidenceScore());
        } catch (BusinessException e) {
            if (e.getErrorCode() == ErrorCode.OCR_TESSERACT_NOT_FOUND) {
                document.setOcrStatus(OcrStatus.SKIPPED);
                log.warn("OCR disabled for document {}: Tesseract not available", document.getId());
            } else {
                document.setOcrStatus(OcrStatus.FAILED);
                log.error("OCR failed for document {}: {}", document.getId(), e.getMessage());
            }
            documentDao.save(document);
        } catch (Exception e) {
            document.setOcrStatus(OcrStatus.FAILED);
            documentDao.save(document);
            log.error("OCR failed for document {}: {}", document.getId(), e.getMessage());
        }
    }

    private void createDigitizedVersion(Document document, OcrService.OcrResult result) {
        try {
            String ext = result.outputFormat() == DigitizeFormat.PDF ? ".pdf" : ".txt";
            String outputFileName = stripExtension(document.getOriginalFilename()) + ext;
            String objectKey = "digitized/" + UUID.randomUUID() + ext;
            String uniqueFilename = objectKey.contains("/")
                    ? objectKey.substring(objectKey.lastIndexOf('/') + 1)
                    : objectKey;

            storageService.upload(
                    new ByteArrayInputStream(result.outputBytes()),
                    objectKey,
                    result.outputFormat() == DigitizeFormat.PDF ? "application/pdf" : "text/plain",
                    result.outputBytes().length
            );

            User uploader = userDao.findById(document.getCreatedBy())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            Document digitizedDocument = Document.builder()
                    .title("Digitized - " + document.getTitle())
                    .description("Digitized from document #" + document.getId() + " (" + document.getTitle() + ")")
                    .originalFilename(outputFileName)
                    .storedFilename(uniqueFilename)
                    .mimeType(result.outputFormat() == DigitizeFormat.PDF ? "application/pdf" : "text/plain")
                    .fileSizeBytes((long) result.outputBytes().length)
                    .storageBucket(storageProperties.getBucket())
                    .storageKey(objectKey)
                    .status(DocumentStatus.PUBLISHED)
                    .visibility(DocumentVisibility.ORGANIZATION)
                    .currentVersionNumber(1)
                    .downloadCount(0)
                    .viewCount(0)
                    .isPasswordProtected(false)
                    .ocrStatus(OcrStatus.COMPLETED)
                    .ocrText(result.text())
                    .organization(document.getOrganization())
                    .build();

            digitizedDocument = documentDao.save(digitizedDocument);

            DocumentVersion digitizedVersion = DocumentVersion.builder()
                    .versionNumber(1)
                    .label("Digitized output (" + result.outputFormat().getDisplayText() + ")")
                    .originalFilename(outputFileName)
                    .storedFilename(uniqueFilename)
                    .storageKey(objectKey)
                    .fileSizeBytes((long) result.outputBytes().length)
                    .mimeType(result.outputFormat() == DigitizeFormat.PDF ? "application/pdf" : "text/plain")
                    .isCurrentVersion(true)
                    .ocrStatus(OcrStatus.COMPLETED)
                    .ocrText(result.text())
                    .document(digitizedDocument)
                    .uploadedBy(uploader)
                    .build();

            documentVersionDao.save(digitizedVersion);

            documentIndexerService.indexDocument(digitizedDocument);

            log.info("Digitized document {} created from document {}: {}",
                    digitizedDocument.getId(), document.getId(), objectKey);
        } catch (Exception e) {
            log.error("Failed to create digitized document from {}: {}", document.getId(), e.getMessage());
        }
    }

    private String stripExtension(String filename) {
        if (filename == null || !filename.contains(".")) return filename;
        return filename.substring(0, filename.lastIndexOf('.'));
    }

    private boolean isOcrSupported(String mimeType) {
        if (mimeType == null) return false;
        return mimeType.equals("image/jpeg")
                || mimeType.equals("image/png")
                || mimeType.equals("image/gif")
                || mimeType.equals("image/webp")
                || mimeType.equals("image/tiff")
                || mimeType.equals("application/pdf")
                || mimeType.equals("text/plain");
    }
}
