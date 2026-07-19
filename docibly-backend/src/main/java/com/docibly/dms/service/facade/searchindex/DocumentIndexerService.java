package com.docibly.dms.service.facade.searchindex;

import com.docibly.dms.bean.core.document.Document;

public interface DocumentIndexerService {

    void indexDocument(Document document);

    void removeDocument(Long documentId);
}
