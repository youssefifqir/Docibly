package com.docibly.dms.service.impl.ocr;

import com.docibly.dms.bean.core.enums.DigitizeFormat;
import com.docibly.dms.exception.BusinessException;
import com.docibly.dms.exception.ErrorCode;
import com.docibly.dms.service.facade.ocr.OcrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class TesseractOcrService implements OcrService {

    private static final Logger log = LoggerFactory.getLogger(TesseractOcrService.class);

    private final String tesseractCommand;
    private final boolean enabled;

    public TesseractOcrService(
            @Value("${app.ocr.tesseract.command:tesseract}") String tesseractCommand,
            @Value("${app.ocr.enabled:true}") boolean enabled) {
        this.tesseractCommand = tesseractCommand;
        this.enabled = enabled;
    }

    @Override
    public OcrResult extractText(InputStream fileStream, String fileName, String language) {
        return extractText(fileStream, fileName, language, DigitizeFormat.NONE);
    }

    @Override
    public OcrResult extractText(InputStream fileStream, String fileName, String language, DigitizeFormat outputFormat) {
        if (!enabled) {
            return new OcrResult("", 0.0, language);
        }

        Path tempInput = null;
        Path tempDir = null;
        try {
            tempDir = Files.createTempDirectory("ocr-");
            String ext = extractExtension(fileName);
            tempInput = tempDir.resolve("input" + ext);
            Files.copy(fileStream, tempInput, StandardCopyOption.REPLACE_EXISTING);

            String outputBase = tempDir.resolve("output").toString();
            String lang = (language != null && !language.isBlank()) ? language : "eng";

            List<String> cmd = new ArrayList<>();
            cmd.add(tesseractCommand);
            cmd.add(tempInput.toAbsolutePath().toString());
            cmd.add(outputBase);

            if (outputFormat == DigitizeFormat.PDF) {
                cmd.add("pdf");
            }

            cmd.add("-l");
            cmd.add(lang);
            cmd.add("--psm");
            cmd.add("3");

            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.directory(tempDir.toFile());
            pb.redirectErrorStream(true);

            log.debug("Running Tesseract: {}", String.join(" ", cmd));
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.trace("Tesseract: {}", line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.warn("Tesseract exited with code {} for file {}", exitCode, fileName);
                return new OcrResult("", 0.0, lang);
            }

            Path txtFile = tempDir.resolve("output.txt");
            String text = "";
            if (Files.exists(txtFile)) {
                text = Files.readString(txtFile).trim();
            }

            double confidence = estimateConfidence(text);

            byte[] outputBytes = null;
            if (outputFormat == DigitizeFormat.PDF) {
                Path pdfFile = tempDir.resolve("output.pdf");
                if (Files.exists(pdfFile)) {
                    outputBytes = Files.readAllBytes(pdfFile);
                    log.debug("OCR PDF generated: {} bytes for {}", outputBytes.length, fileName);
                }
            } else if (outputFormat == DigitizeFormat.TXT) {
                if (Files.exists(txtFile)) {
                    outputBytes = Files.readAllBytes(txtFile);
                }
            }

            log.debug("OCR completed for {}: {} chars, confidence={}", fileName, text.length(), confidence);
            return new OcrResult(text, confidence, lang, outputFormat, outputBytes);

        } catch (IOException e) {
            if (e.getMessage() != null && e.getMessage().contains("CreateProcess")) {
                log.error("Tesseract not found at '{}'. Install Tesseract or set app.ocr.enabled=false", tesseractCommand);
                throw new BusinessException(ErrorCode.OCR_TESSERACT_NOT_FOUND);
            }
            log.error("OCR processing failed for {}: {}", fileName, e.getMessage());
            throw new BusinessException(ErrorCode.OCR_FAILED);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("OCR processing interrupted for {}", fileName);
            throw new BusinessException(ErrorCode.OCR_FAILED);
        } finally {
            cleanup(tempDir);
        }
    }

    private double estimateConfidence(String text) {
        if (text == null || text.isBlank()) return 0.0;
        long alphaNumeric = text.chars().filter(c -> Character.isLetterOrDigit(c)).count();
        long total = text.length();
        if (total == 0) return 0.0;
        double ratio = (double) alphaNumeric / total;
        return Math.min(100.0, Math.max(0.0, ratio * 100.0));
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.')).toLowerCase();
    }

    private void cleanup(Path dir) {
        if (dir != null) {
            try {
                try (var files = Files.walk(dir)) {
                    files.sorted(java.util.Comparator.reverseOrder())
                            .forEach(p -> {
                                try {
                                    Files.deleteIfExists(p);
                                } catch (IOException ignored) {
                                }
                            });
                }
            } catch (IOException ignored) {
            }
        }
    }
}
