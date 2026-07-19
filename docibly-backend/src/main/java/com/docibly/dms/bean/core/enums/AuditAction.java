package com.docibly.dms.bean.core.enums;

import com.docibly.dms.common.bean.BaseEnum;

/**
 * Enum: AuditAction
 */
public enum AuditAction implements BaseEnum {

    DOCUMENT_UPLOADED("Document Uploaded"),
    DOCUMENT_VIEWED("Document Viewed"),
    DOCUMENT_DOWNLOADED("Document Downloaded"),
    DOCUMENT_UPDATED("Document Updated"),
    DOCUMENT_ARCHIVED("Document Archived"),
    DOCUMENT_DELETED("Document Deleted"),
    DOCUMENT_RESTORED("Document Restored"),
    VERSION_CREATED("Version Created"),
    SHARE_CREATED("Share Created"),
    SHARE_REVOKED("Share Revoked"),
    MEMBER_INVITED("Member Invited"),
    MEMBER_REMOVED("Member Removed"),
    OCR_COMPLETED("OCR Completed"),
    FOLDER_CREATED("Folder Created"),
    FOLDER_DELETED("Folder Deleted"),
    ORG_CREATED("Organization Created"),
    ORG_REQUEST_APPROVED("Organization Request Approved"),
    ORG_REQUEST_REJECTED("Organization Request Rejected");

    private final String displayText;

    AuditAction(String displayText) {
        this.displayText = displayText;
    }

    @Override
    public String getDisplayText() {
        return this.displayText;
    }

    /**
     * Find enum by display text (case-insensitive).
     *
     * @param text the display text to search for
     * @return the matching enum value, or null if not found
     */
    public static AuditAction fromDisplayText(String text) {
        if (text == null) return null;
        for (AuditAction value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
