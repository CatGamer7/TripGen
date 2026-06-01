package com.walking.route_generator.model;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public static Mood fromString(String value) {
        for (Mood mood : values()) {
            if (mood.name().equalsIgnoreCase(value) || mood.russianName.equalsIgnoreCase(value)) {
                return mood;
            }
        }
        throw new IllegalArgumentException("Неизвестное значение mood: " + value);
    }
}
