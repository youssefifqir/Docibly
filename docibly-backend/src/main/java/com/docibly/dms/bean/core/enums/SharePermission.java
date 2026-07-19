package com.docibly.dms.bean.core.enums;

import com.docibly.dms.common.bean.BaseEnum;

/**
 * Enum: SharePermission
 */
public enum SharePermission implements BaseEnum {

    VIEW("Can View"),
    COMMENT("Can Comment"),
    EDIT("Can Edit"),
    MANAGE("Can Manage");

    private final String displayText;

    SharePermission(String displayText) {
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
    public static SharePermission fromDisplayText(String text) {
        if (text == null) return null;
        for (SharePermission value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
