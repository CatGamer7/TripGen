package com.walking.route_generator.model;

public enum Tempo {
    SLOW(3.0),    // 3 км/ч
    NORMAL(5.0),  // 5 км/ч
    FAST(7.0);    // 7 км/ч

    private final double kmPerHour;

    Tempo(double kmPerHour) {
        this.kmPerHour = kmPerHour;
    }

    public double getKmPerHour() {
        return kmPerHour;
    }
}
