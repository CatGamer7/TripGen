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


}
