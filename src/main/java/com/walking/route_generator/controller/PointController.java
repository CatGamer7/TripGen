package com.walking.route_generator.controller;

import com.walking.route_generator.model.Point;
import com.walking.route_generator.service.PointService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    // Создание новой точки
    @PostMapping
    public Point createPoint(@RequestBody Point point) {
        return pointService.savePoint(point);
    }

    // Получение всех точек
    @GetMapping
    public List<Point> getAll() {
        return pointService.getAllPoints();
    }

    // Удаление точки
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pointService.deletePoint(id);
    }

}
