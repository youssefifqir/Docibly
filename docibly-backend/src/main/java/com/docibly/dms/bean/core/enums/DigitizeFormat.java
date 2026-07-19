package com.docibly.dms.bean.core.enums;

import com.docibly.dms.common.bean.BaseEnum;

public enum DigitizeFormat implements BaseEnum {

    NONE("None"),
    TXT("Text"),
    PDF("PDF");

    private final String displayText;

    DigitizeFormat(String displayText) {
        this.displayText = displayText;
    }

    @Override
    public String getDisplayText() {
        return this.displayText;
    }

    public static DigitizeFormat fromDisplayText(String text) {
        if (text == null) return NONE;
        for (DigitizeFormat value : values()) {
            if (value.displayText.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return NONE;
    }
}
