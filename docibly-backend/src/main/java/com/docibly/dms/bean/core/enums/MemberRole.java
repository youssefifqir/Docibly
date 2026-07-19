package com.docibly.dms.bean.core.enums;

import com.docibly.dms.common.bean.BaseEnum;

/**
 * Enum: MemberRole
 */
public enum MemberRole implements BaseEnum {

    OWNER("Owner"),
    ADMIN("Admin"),
    MEMBER("Member"),
    VIEWER("Viewer");

    private final String displayText;

    MemberRole(String displayText) {
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
    public static MemberRole fromDisplayText(String text) {
        if (text == null) return null;
        for (MemberRole value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
