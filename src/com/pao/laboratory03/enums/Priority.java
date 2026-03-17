package com.pao.laboratory03.enums;

public enum Priority {

    LOW(1, "green"),
    MEDIUM(2, "yellow"),
    HIGH(3, "orange"),
    CRITICAL(4, "red");

    private final int level;
    private final String color;

    Priority(int level, String color) {
        this.level = level;
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public String getColor() {
        return color;
    }

    public String getEmoji() {
        switch (this) {
            case LOW: return "🟢";
            case MEDIUM: return "🟡";
            case HIGH: return "🟠";
            case CRITICAL: return "🔴";
            default: return "";
        }
    }
}