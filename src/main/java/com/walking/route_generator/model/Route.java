package com.walking.route_generator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "routes")



public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Enumerated(EnumType.STRING)
    private Mood mood;

    private Integer estimatedMinutes; // Расчётное время прогулки

    private String title; // Название маршрута
    private Double totalDistance; //Дистанция маршрута
    private String tempo; //Темп ходьбы

    // Ссылка на владельца маршрута
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Связь
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "rout_id")
    private List<Point> points;

    @Column(columnDefinition = "TEXT")
    private String geometry;

    public Route() {}

    //Getter and Setter
    public Long getId(){
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Double getTotalDistance(){
        return totalDistance;
    }
    public void setTotalDistance(Double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTempo(){
        return tempo;
    }
    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public List<Point> getPoints(){
        return points;
    }
    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public User getUser(){
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Mood getMood() {
        return mood;
    }
    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }
    public void setEstimatedMinutes(Integer estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    public String getGeometry() {
        return geometry;
    }
    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

}
