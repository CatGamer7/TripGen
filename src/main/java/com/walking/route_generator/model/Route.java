package com.walking.route_generator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "routes")

@Getter @Setter

public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title; // Название маршрута
    private Double totalDistance; //Дистанция маршрута
    private String tempo; //Темп ходьбы

    // Ссылка на владельца маршрута
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Связь
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rout_id")
    private List<Point> points;

}
