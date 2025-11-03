package com.agentic.ai.model;

/**
 * 'A' for Arrivals, 'D' for Departures — aligns with Schiphol API semantics.
 */
public enum Direction {
    ARRIVALS("A"),
    DEPARTURES("D");

    private final String code;

    Direction(String code) { this.code = code; }

    public String code() { return code; }

    public static Direction fromCodeOrName(String input) {
        if (input == null) throw new IllegalArgumentException("direction must not be null");
        String v = input.trim().toUpperCase();
        return switch (v) {
            case "A", "ARRIVAL", "ARRIVALS", "IN" -> ARRIVALS;
            case "D", "DEPARTURE", "DEPARTURES", "OUT" -> DEPARTURES;
            default -> throw new IllegalArgumentException("Unsupported direction: " + input +
                    " (use A/ARRIVALS or D/DEPARTURES)");
        };
    }
}
