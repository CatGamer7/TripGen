package com.walking.route_generator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "points")

public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название точки не может быть пустым")
    @Size(min = 2, max = 50, message = "Название должно содержать от 2 до 50 символов")
    private String name; // Название точки

    @NotNull(message = "Широта обязательна")
    @DecimalMin(value = "-90.0", message = "Широта не может быть меньше -90")
    @DecimalMax(value = "90.0", message = "Широта не может быть больше 90")
    private Double latitude; // Широта

    @Size(max = 2000, message = "Описание слишком длинное (максимум 2000 символов)")
    private String description; // Описание

    @NotNull(message = "Долгота обязательна")
    @DecimalMin(value = "-180.0", message = "Долгота не может быть меньше -180")
    @DecimalMax(value = "180.0", message = "Долгота не может быть больше 180")
    private Double longitude; //Долгота

    public Point() {}

    //Getter and Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
