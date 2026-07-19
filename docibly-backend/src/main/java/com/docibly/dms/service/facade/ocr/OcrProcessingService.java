package com.docibly.dms.service.facade.ocr;

import com.docibly.dms.bean.core.enums.DigitizeFormat;

public interface OcrProcessingService {

    void processDocument(Long documentId, DigitizeFormat digitizeFormat);

    void processDocumentVersion(Long documentVersionId);
}
