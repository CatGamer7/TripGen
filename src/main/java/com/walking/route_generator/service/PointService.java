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
    public Point getPointById(Long id) {
        return pointRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Точка с id " + id + " не найдена"
                ));
    }

    // Удалить точку
    public void deletePoint(Long id) {
        if (!pointRepository.existsById(id)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND,
                    "Точка не найдена"
            );
        }
        pointRepository.deleteById(id);
    }
}
