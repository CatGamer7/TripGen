package com.walking.route_generator.dto;

import com.walking.route_generator.model.Mood;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RouteGenerationRequestDto {
    @NotNull(message = "Настроение обязательно")
    private Mood mood;

    @NotNull
    @DecimalMin(value = "0.5", message = "Минимальная дистанция — 0.5 км")
    @DecimalMax(value = "50.0", message = "Максимальная дистанция — 50 км")
    private Double desiredDistanceKm; // Желаемая дистанция в км

    @NotBlank
    private String tempo; // "SLOW", "NORMAL", "FAST"


    // Координаты пользователя — старт и финиш маршрута
    @NotNull(message = "Широта пользователя обязательна")
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double userLatitude;

    @NotNull(message = "Долгота пользователя обязательна")
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double userLongitude;


    // Getters / Setters
    public Mood getMood() { return mood; }
    public void setMood(Mood mood) { this.mood = mood; }

    public Double getDesiredDistanceKm() { return desiredDistanceKm; }
    public void setDesiredDistanceKm(Double desiredDistanceKm) { this.desiredDistanceKm = desiredDistanceKm; }

    public String getTempo() { return tempo; }
    public void setTempo(String tempo) { this.tempo = tempo; }

    public Double getUserLatitude() { return userLatitude; }
    public void setUserLatitude(Double lat) { this.userLatitude = lat; }

    public Double getUserLongitude() { return userLongitude; }
    public void setUserLongitude(Double lon) { this.userLongitude = lon; }

}
