package com.docibly.dms.bean.core.enums;

import com.docibly.dms.common.bean.BaseEnum;

/**
 * Enum: DocumentVisibility
 */
public enum DocumentVisibility implements BaseEnum {

    PRIVATE("Private"),
    TEAM("Team"),
    ORGANIZATION("Organization"),
    PUBLIC("Public");

    private final String displayText;

    DocumentVisibility(String displayText) {
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
    public static DocumentVisibility fromDisplayText(String text) {
        if (text == null) return null;
        for (DocumentVisibility value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}
