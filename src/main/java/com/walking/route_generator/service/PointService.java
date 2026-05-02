package com.walking.route_generator.service;

import com.walking.route_generator.repository.PointRepository;
import org.springframework.stereotype.Service;
import com.walking.route_generator.model.Point;
import java.util.List;
import java.util.Optional;

@Service
public class PointService {
    private final PointRepository pointRepository;
    public PointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    // Создать/обновить точку
    public Point savePoint(Point point) {
        return pointRepository.save(point);
    }

    // Получить все точки
    public List<Point> getAllPoints() {
        return pointRepository.findAll();
    }

    // Найти точку по ID
    public Optional<Point> getPointById(Long id) {
        return pointRepository.findById(id);
    }

    // Удалить точку
    public void deletePoint(Long id) {
        pointRepository.deleteById(id);
    }
}
