package com.docibly.dms.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    EMAIL_ALREADY_EXISTS("ERR_EMAIL_EXISTS", "Email already exists", CONFLICT),
    PHONE_ALREADY_EXISTS("ERR_PHONE_EXISTS", "An account with this phone number already exists", CONFLICT),
    PASSWORD_MISMATCH("ERR_PASSWORD_MISMATCH", "The password and confirmation do not match", BAD_REQUEST),
    CHANGE_PASSWORD_MISMATCH("ERR_PASSWORD_MISMATCH", "New password and confirmation do not match", BAD_REQUEST),
    ERR_SENDING_ACTIVATION_EMAIL("ERR_SENDING_ACTIVATION_EMAIL",
            "An error occurred while sending the activation email",
            INTERNAL_SERVER_ERROR),
    ERR_USER_DISABLED("ERR_USER_DISABLED",
            "User account is disabled, please activate your account or contact the administrator",
            UNAUTHORIZED),
    INVALID_CURRENT_PASSWORD("INVALID_CURRENT_PASSWORD", "The current password is incorrect", BAD_REQUEST),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found", NOT_FOUND),
    ROLE_NOT_FOUND("ROLE_NOT_FOUND", "Role not found", NOT_FOUND),
    ACCOUNT_ALREADY_DEACTIVATED("ACCOUNT_ALREADY_DEACTIVATED", "Account has been deactivated", BAD_REQUEST),
    BAD_CREDENTIALS("BAD_CREDENTIALS", "Username and / or password is incorrect", UNAUTHORIZED),
    INTERNAL_EXCEPTION("INTERNAL_EXCEPTION",
            "An internal exception occurred, please try again or contact the admin",
            INTERNAL_SERVER_ERROR),
    UNKNOWN_ERROR("UNKNOWN_ERROR", "An unexpected error occurred", INTERNAL_SERVER_ERROR),
    USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "Cannot find user with the provided username", NOT_FOUND),

    STORAGE_FILE_EMPTY("STORAGE_FILE_EMPTY", "The uploaded file is empty", BAD_REQUEST),
    STORAGE_FILE_TOO_LARGE("STORAGE_FILE_TOO_LARGE", "The uploaded file exceeds the maximum allowed size", BAD_REQUEST),
    STORAGE_CONTENT_TYPE_NOT_ALLOWED("STORAGE_CONTENT_TYPE_NOT_ALLOWED", "The file content type is not permitted", BAD_REQUEST),
    STORAGE_UPLOAD_FAILED("STORAGE_UPLOAD_FAILED", "File upload to storage failed", INTERNAL_SERVER_ERROR),
    STORAGE_DOWNLOAD_FAILED("STORAGE_DOWNLOAD_FAILED", "File download from storage failed", INTERNAL_SERVER_ERROR),
    STORAGE_DELETE_FAILED("STORAGE_DELETE_FAILED", "File deletion from storage failed", INTERNAL_SERVER_ERROR),
    STORAGE_PRESIGN_FAILED("STORAGE_PRESIGN_FAILED", "Failed to generate presigned URL", INTERNAL_SERVER_ERROR),

    TOKEN_INVALID("TOKEN_INVALID", "The provided token is invalid", BAD_REQUEST),
    TOKEN_EXPIRED("TOKEN_EXPIRED", "The provided token has expired", BAD_REQUEST),
    TOKEN_ALREADY_USED("TOKEN_ALREADY_USED", "This token has already been used", BAD_REQUEST),

    ORG_NOT_FOUND("ORG_NOT_FOUND", "Organization not found", NOT_FOUND),
    ORG_SLUG_TAKEN("ORG_SLUG_TAKEN", "An organization with this slug already exists", CONFLICT),
    ORG_MEMBER_NOT_FOUND("ORG_MEMBER_NOT_FOUND", "User is not a member of this organization", FORBIDDEN),
    ORG_MEMBER_ALREADY_EXISTS("ORG_MEMBER_ALREADY_EXISTS", "User is already a member of this organization", CONFLICT),
    ORG_ACCESS_DENIED("ORG_ACCESS_DENIED", "Insufficient role privileges for this operation", FORBIDDEN),
    ORG_REQUIRED("ORG_REQUIRED", "X-Org-Id header is required for this endpoint", BAD_REQUEST),
    ORG_INVALID_ROLE("ORG_INVALID_ROLE", "Cannot assign this role to an invited member", BAD_REQUEST),
    ORG_INSUFFICIENT_PERMISSIONS("ORG_INSUFFICIENT_PERMISSIONS", "You cannot assign a role higher than your own", FORBIDDEN),
    ORG_CANNOT_REMOVE_SELF("ORG_CANNOT_REMOVE_SELF", "You cannot remove yourself from the organization", BAD_REQUEST),
    ORG_INVITATION_NOT_FOUND("ORG_INVITATION_NOT_FOUND", "Invitation not found", NOT_FOUND),
    ORG_INVITATION_EXPIRED("ORG_INVITATION_EXPIRED", "Invitation has expired or is no longer valid", GONE),
    ORG_INVITATION_WRONG_USER("ORG_INVITATION_WRONG_USER", "This invitation was sent to a different email address", FORBIDDEN),

    DOCUMENT_NOT_FOUND("DOCUMENT_NOT_FOUND", "Document not found", NOT_FOUND),
    DOCUMENT_VERSION_NOT_FOUND("DOCUMENT_VERSION_NOT_FOUND", "Document version not found", NOT_FOUND),
    DOCUMENT_STORAGE_FAILED("DOCUMENT_STORAGE_FAILED", "Failed to store document file", INTERNAL_SERVER_ERROR),
    DOCUMENT_DOWNLOAD_FAILED("DOCUMENT_DOWNLOAD_FAILED", "Failed to download document file", INTERNAL_SERVER_ERROR),

    OCR_FAILED("OCR_FAILED", "Optical character recognition processing failed", INTERNAL_SERVER_ERROR),
    OCR_TESSERACT_NOT_FOUND("OCR_TESSERACT_NOT_FOUND", "Tesseract OCR engine is not installed or not found on PATH", INTERNAL_SERVER_ERROR),

    SHARE_NOT_FOUND("SHARE_NOT_FOUND", "Share link not found", NOT_FOUND),
    SHARE_REVOKED("SHARE_REVOKED", "This share link has been revoked", GONE),
    SHARE_EXPIRED("SHARE_EXPIRED", "This share link has expired", GONE),
    SHARE_PASSWORD_REQUIRED("SHARE_PASSWORD_REQUIRED", "This share link requires a password", UNAUTHORIZED),
    SHARE_PASSWORD_INVALID("SHARE_PASSWORD_INVALID", "Incorrect password for this share link", UNAUTHORIZED),

    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND", "Comment not found", NOT_FOUND),
    COMMENT_NOT_OWNER("COMMENT_NOT_OWNER", "You can only edit your own comments", FORBIDDEN),
    COMMENT_PARENT_MISMATCH("COMMENT_PARENT_MISMATCH", "Parent comment does not belong to the same document", BAD_REQUEST),

    NOTIFICATION_NOT_FOUND("NOTIFICATION_NOT_FOUND", "Notification not found", NOT_FOUND),
    NOTIFICATION_NOT_OWNER("NOTIFICATION_NOT_OWNER", "This notification does not belong to you", FORBIDDEN),

    DEPT_NOT_FOUND("DEPT_NOT_FOUND", "Department not found", NOT_FOUND),
    DEPT_MEMBER_NOT_FOUND("DEPT_MEMBER_NOT_FOUND", "Department member not found", NOT_FOUND),
    DEPT_MEMBER_ALREADY_EXISTS("DEPT_MEMBER_ALREADY_EXISTS", "User is already a member of this department", CONFLICT),
    DEPT_SHARE_NOT_FOUND("DEPT_SHARE_NOT_FOUND", "Department document share not found", NOT_FOUND),
    DEPT_INVALID_PARENT("DEPT_INVALID_PARENT", "Invalid parent department", BAD_REQUEST),
    DEPT_HAS_SUBDEPARTMENTS("DEPT_HAS_SUBDEPARTMENTS", "Cannot delete a department that has sub-departments", CONFLICT),
    DEPT_ROLE_NOT_FOUND("DEPT_ROLE_NOT_FOUND", "Department role not found", NOT_FOUND),
    DEPT_ROLE_DUPLICATE("DEPT_ROLE_DUPLICATE", "A role with this name already exists", CONFLICT),
    DEPT_ROLE_IN_USE("DEPT_ROLE_IN_USE", "Cannot delete a role that is assigned to members", CONFLICT),
    DEPT_PERMISSION_DENIED("DEPT_PERMISSION_DENIED", "You do not have permission for this action", FORBIDDEN);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

    ErrorCode(final String code,
              final String defaultMessage,
              final HttpStatus status) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;
    }
}
