# Frontend ↔ Backend Wiring Plan

## Pre-requisite: Unify Types

**Why:** Frontend has `Document` (mock type) and `DocumentDto` (service type) — they differ. Every page/service needs one source of truth matching the backend DTOs exactly.

**Action:** Create a single `src/types/api.ts` with all backend-backed DTOs. Remove duplicate inline interfaces from service files.

## Phase 1: Foundation (Auth + Org Context)

| Step | File(s) | What |
|------|---------|------|
| 1.1 | `api.ts` | Add `X-Org-Id` header from `localStorage` to every request. Add `X-Org-Id` to the refresh retry logic. |
| 1.2 | `AuthContext.tsx` | On login, call `POST /api/v1/organization-flow/workspace` to create workspace (first time) or store the org ID from profile. Persist `orgId` alongside tokens. |
| 1.3 | `AuthContext.tsx` | Fetch user profile from `GET /api/v1/user/profile` at startup to hydrate `User` state. |

**Milestone:** User can log in, workspace is created, every API call includes `Authorization` + `X-Org-Id`.

## Phase 2: Core CRUD (Documents + Tags + Folders)

| Step | File(s) | What |
|------|---------|------|
| 2.1 | `types/api.ts` | Define `DocumentDto`, `DocumentVersionDto`, `TagDto`, `FolderDto`, `PageResponse<T>` matching backend DTOs. |
| 2.2 | `services/documents.service.ts` | Rewrite — endpoints: `GET /api/v1/user/documents` (paginated, org-scoped), `GET /api/v1/user/documents/{id}`, `POST /api/v1/user/documents/upload` (multipart + digitizeFormat), `PUT /api/v1/user/documents/{id}`, `DELETE /api/v1/user/documents/{id}`. Remove old `/member/documents` paths. |
| 2.3 | `services/tags.service.ts` | Rewrite — `GET /api/v1/user/tags`, `POST /api/v1/user/tags`, `DELETE /api/v1/user/tags/{id}`. |
| 2.4 | `services/folders.service.ts` | Rewrite — `GET /api/v1/user/folders`, `POST /api/v1/user/folders`, `DELETE /api/v1/user/folders/{id}`. |
| 2.5 | `LibraryPage.tsx` | Replace mock-backed `useDocuments` hook with direct service calls. Wire grid/list, tab switcher (All/My/Shared), delete, preview. |
| 2.6 | `UploadPage.tsx` | Add `digitizeFormat` field (NONE/TXT/PDF). Send `X-Org-Id`. Wire real tag selector. |
| 2.7 | `DashboardPage.tsx` | Replace stat card values with real API data (total count, storage used, recent shared count). |
| 2.8 | `TagsPage.tsx` | Already partially wired — verify against real backend. |
| 2.9 | `EntitiesPage.tsx` | Rewrite or remove — backend has no `AdministrativeEntity`. Replace with org member list or remove. |

**Milestone:** Documents, tags, and folders fully functional. Upload with digitize format works.

## Phase 3: Search

| Step | File(s) | What |
|------|---------|------|
| 3.1 | `services/search.service.ts` | New service — `GET /api/v1/search?q=...&page=...&size=...` with org-scoped Elasticsearch. |
| 3.2 | `SearchPage.tsx` | Replace mock data. Wire real results, filter pills (file type, tags from backend), pagination. |

**Milestone:** Full-text search across documents works.

## Phase 4: Organization Flow (Members)

| Step | File(s) | What |
|------|---------|------|
| 4.1 | `services/organization.service.ts` | New service — invite (`POST /api/v1/organization-flow/invite`), accept (`POST /api/v1/organization-flow/invitations/{token}/accept`), list members (`GET /api/v1/user/organization-members`), change role (`PUT /api/v1/organization-flow/members/{id}/role`), remove (`DELETE /api/v1/organization-flow/members/{id}`). |
| 4.2 | `SharedPage.tsx` | Replace mock departments + members with real org member list. Keep the layout (left tree / center docs / right panel) but back it with real data. |
| 4.3 | *new* `MembersPage.tsx` | (Optional) Dedicated page to manage members — invite by email, change roles, remove. |

**Milestone:** Users can invite teammates and manage the org.

## Phase 5: Sharing (Public Links)

| Step | File(s) | What |
|------|---------|------|
| 5.1 | `services/shares.service.ts` | New service — `POST /api/v1/user/shares` (create with permission, expiry, password), `DELETE /api/v1/user/shares/{id}` (revoke), `GET /api/v1/user/shares?documentId=...` (list shares for a doc). |
| 5.2 | `ShareModal.tsx` | Wire the existing modal to real create/revoke endpoints. Add password field, expiry picker. |
| 5.3 | *new* `PublicSharePage.tsx` | (Optional) Page at `/share/{token}` that calls `GET /api/v1/public/shares/{token}` — renders file preview + download button. No auth. |

**Milestone:** Users can create password-protected share links and revoke them.

## Phase 6: Comments

| Step | File(s) | What |
|------|---------|------|
| 6.1 | `services/comments.service.ts` | New service — `GET /api/v1/user/documents/{docId}/comments`, `POST /api/v1/user/documents/{docId}/comments`, `PUT /api/v1/user/documents/{docId}/comments/{id}`, `DELETE /api/v1/user/documents/{docId}/comments/{id}`, `POST /api/v1/user/documents/{docId}/comments/{id}/resolve`. |
| 6.2 | `DocumentPreview.tsx` | Add a comments panel — display threaded comments, reply input, edit/delete own comments, resolve toggle. |

**Milestone:** Users can comment on documents with threading and resolve.

## Phase 7: Notifications

| Step | File(s) | What |
|------|---------|------|
| 7.1 | `services/notifications.service.ts` | New service — `GET /api/v1/user/notifications`, `GET /api/v1/user/notifications/unread-count`, `POST /api/v1/user/notifications/{id}/read`, `POST /api/v1/user/notifications/read-all`. |
| 7.2 | `Navbar.tsx` | Add bell icon with unread badge (poll or periodic fetch). Add notification dropdown listing recent notifications with read/unread styling. |

**Milestone:** Users see notifications for invites, comments, OCR completion.

## Phase 8: Versioning + Digitization

| Step | File(s) | What |
|------|---------|------|
| 8.1 | `DocumentPreview.tsx` | Add version history panel — list versions, download specific version, re-upload trigger. |
| 8.2 | `DocumentPreview.tsx` | Show OCR status badge (PENDING/PROCESSING/COMPLETED/FAILED). Show OCR text if available. |
| 8.3 | `DigitizationPage.tsx` | Replace mock OCR simulation with real upload + `digitizeFormat=PDF`. Poll document status until OCR completes, then show downloadable PDF. |

**Milestone:** Full version history, OCR status visible, digitization page works end-to-end.

## Phase 9: Audit Log + Profile

| Step | File(s) | What |
|------|---------|------|
| 9.1 | `services/audit.service.ts` | New service — `GET /api/v1/user/auditlogs/org/{orgId}`. |
| 9.2 | *new* `AuditLogPage.tsx` | Table of audit events — filter by action type, entity type, date range. Read-only (admin/owner only). |
| 9.3 | `ProfilePage.tsx` | Wire save button to `PUT /api/v1/user/profile` (if endpoint exists) or leave as local state for MVP. |

**Milestone:** Admins can view the audit trail. Profile page works.

## Phase 10: Cleanup

| Step | File(s) | What |
|------|---------|------|
| 10.1 | `src/data/mock.ts` | Delete entirely (or archive). No more mock data. |
| 10.2 | `src/hooks/useDocuments.ts` | Delete — replaced by direct service calls. |
| 10.3 | `src/types/index.ts` | Remove old mock `Document`, `User`, `Tag`, `Activity`, `Department`, `Message` types — replaced by `types/api.ts`. Keep only what's needed for pure UI state (e.g. `ViewMode`, `SortField`, `DocumentFilters`). |
| 10.4 | Service files | Remove inline `interface` definitions — import from `types/api.ts`. |

**Milestone:** Zero mock data. All pages backed by real API.
