package com.docibly.dms.service.facade.ocr;

import com.docibly.dms.bean.core.enums.DigitizeFormat;

import java.io.InputStream;

public interface OcrService {

    OcrResult extractText(InputStream fileStream, String fileName, String language);

    OcrResult extractText(InputStream fileStream, String fileName, String language, DigitizeFormat outputFormat);

    record OcrResult(String text, double confidenceScore, String language,
                     DigitizeFormat outputFormat, byte[] outputBytes) {

        public OcrResult(String text, double confidenceScore, String language) {
            this(text, confidenceScore, language, DigitizeFormat.NONE, null);
        }
    }
}
