package com.docibly.dms.ws.controller.share;

import com.docibly.dms.bean.core.document.DocumentShare;
import com.docibly.dms.service.facade.document.DocumentShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/public/shares")
@RequiredArgsConstructor
public class PublicShareController {

    private final DocumentShareService documentShareService;

    @GetMapping("/{token}")
    public ResponseEntity<?> accessShare(
            @PathVariable String token,
            @RequestParam(value = "password", required = false) String password) {
        DocumentShareService.DocumentInfo info = documentShareService.accessShare(token, password);
        DocumentShare share = info.share();
        return ResponseEntity.ok(Map.of(
                "share", Map.of(
                        "token", share.getShareToken(),
                        "permission", share.getPermission().getDisplayText(),
                        "expiresAt", share.getExpiresAt()
                ),
                "document", Map.of(
                        "id", info.document().getId(),
                        "title", info.document().getTitle(),
                        "description", info.document().getDescription(),
                        "mimeType", info.document().getMimeType(),
                        "fileSizeBytes", info.document().getFileSizeBytes(),
                        "originalFilename", info.document().getOriginalFilename()
                )
        ));
    }

    @GetMapping("/{token}/download")
    public ResponseEntity<Resource> downloadShare(
            @PathVariable String token,
            @RequestParam(value = "password", required = false) String password) {
        DocumentShareService.DocumentInfo info = documentShareService.accessShare(token, password);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(info.document().getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + info.document().getOriginalFilename() + "\"")
                .body(info.resource());
    }

    @PostMapping("/{token}/verify-password")
    public ResponseEntity<Map<String, Boolean>> verifyPassword(
            @PathVariable String token,
            @RequestBody Map<String, String> body) {
        String password = body.get("password");
        DocumentShare share = documentShareService.verifyPassword(token, password);
        return ResponseEntity.ok(Map.of("valid", true));
    }
}
