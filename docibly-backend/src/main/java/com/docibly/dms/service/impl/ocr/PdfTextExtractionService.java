package com.docibly.dms.service.impl.ocr;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class PdfTextExtractionService {

    private static final Logger log = LoggerFactory.getLogger(PdfTextExtractionService.class);

    public PdfTextResult extractText(InputStream pdfStream) {
        try (PDDocument document = Loader.loadPDF(pdfStream.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            String text = stripper.getText(document);
            if (text == null) {
                text = "";
            }
            text = text.trim();
            log.debug("PDF text extraction: {} chars", text.length());
            return new PdfTextResult(text, text.length() > 100);
        } catch (IOException e) {
            log.warn("PDF text extraction failed: {}", e.getMessage());
            return new PdfTextResult("", false);
        }
    }

    public record PdfTextResult(String text, boolean hasSubstantialText) {}
}
