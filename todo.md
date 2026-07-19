High Priority
1. Org RBAC — The biggest item. Build:
- OrgContextFilter — extracts X-Org-Id header, resolves OrganizationMember, puts it in request scope
- OrgAuthorizationService — reusable helper: requireRole(orgId, userId, minRole), isAtLeast(memberRole, minRole)
- Every org-scoped service method starts with orgAuth.requireRole(req.orgId, currentUser, MEMBER)
2. Document upload/download — MinIO wiring:
- DocumentStorageService.upload(file, orgId) → stores in MinIO, creates Document + DocumentVersion
- DocumentStorageService.download(documentId, versionNumber?) → presigned URL or stream
- File validation (type check, size check, virus scan later)
3. OCR pipeline:
- OcrService — calls Tesseract via subprocess or Tess4J lib
- OcrProcessingService — @Async method, runs after upload, updates ocrStatus from PENDING → PROCESSING → COMPLETED/FAILED
- Wire into upload flow: after file saved, fire async OCR
4. Elasticsearch:
- Add spring-boot-starter-data-elasticsearch to generated pom
- DocumentSearchIndexer — listens to document create/update/delete, pushes to ES index
- SearchController — GET /api/v1/search?q=&orgId= → returns search results
- TODO: decide whether SearchIndex entity is needed if ES handles it; could drop the entity
5. Organization flow:
- OrgRequestService — USER submits request → ADMIN approves → Organization + OrganizationMember(OWNER) created
- MemberService.invite() → create OrganizationInvitation + send email
- MemberService.acceptInvite(token) → create OrganizationMember
- MemberService.removeMember() + role change endpoints
Medium Priority
6. Document sharing — ShareService.createShare(docId, permission, expiresAt) → generates shareToken (UUID), stores in DocumentShare. Public link endpoint validates token + password.
7. Versioning — on re-upload: increment currentVersionNumber, create new DocumentVersion, set isCurrentVersion=false on old one. DocumentVersionService.restore(versionId) copies that version's file as the new current.
8. Comments — CommentService.create() checks doc visibility + org membership. Threaded via parentComment + replies set.
9. Notifications — fire NotificationService.notify(userId, type, title, message, targetUrl) inside service methods (upload, share, comment, etc.). read field toggled by user.
10. Audit logging — inject AuditLogService.log(action, actorId, targetType, targetId, orgId, metadata) into every write operation.
Low Priority
