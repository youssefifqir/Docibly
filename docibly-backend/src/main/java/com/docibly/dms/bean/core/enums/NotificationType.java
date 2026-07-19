package com.docibly.dms.bean.core.enums;

import com.docibly.dms.common.bean.BaseEnum;

/**
 * Enum: NotificationType
 */
public enum NotificationType implements BaseEnum {

    DOCUMENT_SHARED("Document Shared With You"),
    COMMENT_ADDED("New Comment"),
    MEMBER_JOINED("Member Joined"),
    INVITATION_RECEIVED("Invitation Received"),
    OCR_READY("OCR Processing Complete"),
    STORAGE_LIMIT_WARNING("Storage Limit Warning"),
    ORG_REQUEST_APPROVED("Organization Request Approved"),
    ORG_REQUEST_REJECTED("Organization Request Rejected");

    private final String displayText;

    NotificationType(String displayText) {
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
    public static NotificationType fromDisplayText(String text) {
        if (text == null) return null;
        for (NotificationType value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
