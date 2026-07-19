package com.docibly.dms.bean.core.enums;

import com.docibly.dms.common.bean.BaseEnum;

/**
 * Enum: OrgRequestStatus
 */
public enum OrgRequestStatus implements BaseEnum {

    PENDING("Pending Review"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String displayText;

    OrgRequestStatus(String displayText) {
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
    public static OrgRequestStatus fromDisplayText(String text) {
        if (text == null) return null;
        for (OrgRequestStatus value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
