export interface PageResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

export interface AuthResponse {
  access_token: string
  refresh_token: string
  token_type: string
}

export interface UserProfile {
  id: string
  email: string
  firstName: string
  lastName: string
  roles: string[]
  enabled: boolean
  emailVerified: boolean
}

export interface DocumentDto {
  id: number
  ref?: string
  createdDate?: string
  lastModifiedDate?: string
  title: string
  description?: string
  originalFilename?: string
  storedFilename?: string
  mimeType?: string
  fileSizeBytes?: number
  status?: string
  visibility?: string
  currentVersionNumber?: number
  downloadCount?: number
  viewCount?: number
  isPasswordProtected?: boolean
  expiresAt?: string
  archivedAt?: string
  ocrStatus?: string
  ocrText?: string
  ocrProcessedAt?: string
  ocrLanguage?: string
  ocrConfidenceScore?: number
}

export interface DocumentVersionDto {
  id: number
  ref?: string
  createdDate?: string
  lastModifiedDate?: string
  versionNumber: number
  label?: string
  changeNote?: string
  originalFilename?: string
  storedFilename?: string
  fileSizeBytes?: number
  mimeType?: string
  isCurrentVersion?: boolean
  ocrStatus?: string
  ocrText?: string
}

export interface DocumentShareDto {
  id: number
  ref?: string
  createdDate?: string
  lastModifiedDate?: string
  permission: string
  sharedWithEmail?: string
  isPublicLink?: boolean
  expiresAt?: string
  isRevoked?: boolean
  accessCount?: number
  lastAccessedAt?: string
  requiresPassword?: boolean
}

export interface DocumentCommentDto {
  id: number
  content: string
  isResolved?: boolean
  isEdited?: boolean
  createdDate?: string
  editedAt?: string
  authorId?: string
  authorEmail?: string
  parentCommentId?: number
}

export interface TagDto {
  id: number
  ref?: string
  createdDate?: string
  lastModifiedDate?: string
  name: string
  slug?: string
  color?: string
  usageCount?: number
  organizationId?: number
}

export interface FolderDto {
  id: number
  ref?: string
  createdDate?: string
  lastModifiedDate?: string
  name: string
  description?: string
  color?: string
  iconName?: string
  isShared?: boolean
  documentCount?: number
  totalSizeBytes?: number
  parentFolderId?: number
  organizationId?: number
}

export interface OrganizationDto {
  id: number
  ref?: string
  createdDate?: string
  lastModifiedDate?: string
  name: string
  slug: string
  description?: string
  logoUrl?: string
  website?: string
  storageUsedBytes?: number
  storageQuotaBytes?: number
  maxMembers?: number
  isActive?: boolean
}

export interface OrganizationMemberDto {
  id: number
  ref?: string
  createdDate?: string
  lastModifiedDate?: string
  memberRole: string
  joinedAt?: string
  isActive?: boolean
}

export interface OrganizationInvitationDto {
  id: number
  ref?: string
  createdDate?: string
  lastModifiedDate?: string
  inviteeEmail: string
  intendedRole: string
  status: string
  expiresAt?: string
  acceptedAt?: string
}

export interface SearchResultDto {
  documentId: number
  title: string
  description?: string
  content?: string
  originalFilename?: string
  mimeType?: string
  tags?: string
  organizationId?: number
  visibility?: string
  createdDate?: string
  ownerId?: string
}

export interface SearchResponse {
  results: SearchResultDto[]
  totalHits: number
  page: number
  size: number
}

export interface NotificationDto {
  id: number
  ref?: string
  createdDate?: string
  lastModifiedDate?: string
  type: string
  title: string
  message?: string
  read?: boolean
  readAt?: string
  targetUrl?: string
  relatedEntityType?: string
  relatedEntityId?: string
  recipientId?: string
}

export interface AuditLogDto {
  id: number
  ref?: string
  createdDate?: string
  lastModifiedDate?: string
  action: string
  actorUserId?: string
  actorEmail?: string
  targetEntityType: string
  targetEntityId: string
  organizationId?: number
  metadata?: string
}

export enum DocumentStatus {
  DRAFT = 'Draft',
  PUBLISHED = 'Published',
  ARCHIVED = 'Archived',
  DELETED = 'Deleted',
}

export enum DocumentVisibility {
  PRIVATE = 'Private',
  TEAM = 'Team',
  ORGANIZATION = 'Organization',
  PUBLIC = 'Public',
}

export enum OcrStatus {
  PENDING = 'Pending',
  PROCESSING = 'Processing',
  COMPLETED = 'Completed',
  FAILED = 'Failed',
  SKIPPED = 'Skipped',
}

export enum SharePermission {
  VIEW = 'Can View',
  COMMENT = 'Can Comment',
  EDIT = 'Can Edit',
  MANAGE = 'Can Manage',
}

export enum MemberRole {
  OWNER = 'Owner',
  ADMIN = 'Admin',
  MEMBER = 'Member',
  VIEWER = 'Viewer',
}

export enum NotificationType {
  DOCUMENT_SHARED = 'Document Shared With You',
  COMMENT_ADDED = 'New Comment',
  MEMBER_JOINED = 'Member Joined',
  INVITATION_RECEIVED = 'Invitation Received',
  OCR_READY = 'OCR Processing Complete',
}

export enum DigitizeFormat {
  NONE = 'None',
  TXT = 'Text',
  PDF = 'PDF',
}

export interface WorkspaceCreateRequest {
  name: string
  slug: string
  description?: string
}

export interface WorkspaceCreateResponse {
  id: number
  name: string
  slug: string
}

export interface InviteMemberRequest {
  email: string
  role?: string
}

export interface InviteMemberResponse {
  id: number
  token: string
  email: string
  role: string
  expiresAt: string
}

export interface AcceptInvitationRequest {
  token: string
}

export interface AcceptInvitationResponse {
  id: number
  orgId: number
  role: string
}

export interface ChangeRoleRequest {
  role: string
}

export interface CreateShareRequest {
  permission: string
  isPublicLink?: boolean
  expiresAt?: string
  password?: string
}

export interface CreateCommentRequest {
  content: string
  parentCommentId?: number
}

export interface ProfileResponse {
  id: string
  email: string
  firstName: string
  lastName: string
  phoneNumber?: string
  avatarUrl?: string
  bio?: string
  jobTitle?: string
  enabled: boolean
  emailVerified: boolean
  createdDate?: string
  planTier?: string
  storageUsedBytes?: number
  storageQuotaBytes?: number
  authProvider?: string
  roles?: string[]
}

export interface ProfileUpdateRequest {
  firstName?: string
  lastName?: string
  avatarUrl?: string
  bio?: string
  jobTitle?: string
}
