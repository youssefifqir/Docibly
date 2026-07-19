package com.docibly.dms.service.facade.document;

import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.enums.DigitizeFormat;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentStorageService {

    Document upload(Long orgId, MultipartFile file, String title, String description, Long folderId, List<Long> tagIds);

    Document upload(Long orgId, MultipartFile file, String title, String description, Long folderId, List<Long> tagIds, DigitizeFormat digitizeFormat);

    Resource download(Long documentId);

    String getPresignedUrl(Long documentId);
}
