package com.docibly.dms.ws.controller.document.user;

import com.docibly.dms.bean.core.document.DocumentComment;
import com.docibly.dms.service.facade.document.DocumentCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/documents/{documentId}/comments")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class DocumentCommentUserRestController {

    private final DocumentCommentService documentCommentService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long documentId,
            @RequestHeader("X-Org-Id") Long orgId) {
        List<DocumentComment> comments = documentCommentService.getComments(documentId, orgId);
        List<CommentResponse> responses = comments.stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long documentId,
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestBody Map<String, Object> body) {
        String content = (String) body.get("content");
        Long parentCommentId = body.get("parentCommentId") != null
                ? Long.valueOf(body.get("parentCommentId").toString()) : null;
        DocumentComment comment = documentCommentService.addComment(documentId, orgId, content, parentCommentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(comment));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> editComment(
            @PathVariable Long documentId,
            @PathVariable Long commentId,
            @RequestHeader("X-Org-Id") Long orgId,
            @RequestBody Map<String, String> body) {
        DocumentComment comment = documentCommentService.editComment(commentId, orgId, body.get("content"));
        return ResponseEntity.ok(toResponse(comment));
    }

    @PostMapping("/{commentId}/resolve")
    public ResponseEntity<CommentResponse> toggleResolve(
            @PathVariable Long documentId,
            @PathVariable Long commentId,
            @RequestHeader("X-Org-Id") Long orgId) {
        DocumentComment comment = documentCommentService.resolveComment(commentId, orgId);
        return ResponseEntity.ok(toResponse(comment));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long documentId,
            @PathVariable Long commentId,
            @RequestHeader("X-Org-Id") Long orgId) {
        documentCommentService.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }

    private CommentResponse toResponse(DocumentComment c) {
        return new CommentResponse(
                c.getId(), c.getContent(), c.getIsResolved(), c.getIsEdited(),
                c.getCreatedDate(), c.getEditedAt(),
                c.getAuthor() != null ? c.getAuthor().getId() : null,
                c.getAuthor() != null ? c.getAuthor().getEmail() : null,
                c.getParentComment() != null ? c.getParentComment().getId() : null
        );
    }

    public record CommentResponse(
            Long id, String content, Boolean isResolved, Boolean isEdited,
            LocalDateTime createdDate, LocalDateTime editedAt,
            String authorId, String authorEmail, Long parentCommentId
    ) {}
}
