package com.docibly.dms.bean.core.enums;

import com.docibly.dms.common.bean.BaseEnum;

/**
 * Enum: InvitationStatus
 */
public enum InvitationStatus implements BaseEnum {

    PENDING("Pending"),
    ACCEPTED("Accepted"),
    DECLINED("Declined"),
    EXPIRED("Expired"),
    REVOKED("Revoked");

    private final String displayText;

    InvitationStatus(String displayText) {
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
    public static InvitationStatus fromDisplayText(String text) {
        if (text == null) return null;
        for (InvitationStatus value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
