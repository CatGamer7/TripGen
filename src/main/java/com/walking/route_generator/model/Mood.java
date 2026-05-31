package com.walking.route_generator.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Mood {
    SPOKOINOE("Спокойное"),
    ACTIVNOE("Активное"),
    IZBRANOE("Избранное"),
    POZNAVATELNOE("Познавательное");

    private final String russianName;

    Mood(String russianName) {
        this.russianName = russianName;
    }

    @JsonValue
    public String getRussianName() {
        return russianName;
    }
}
