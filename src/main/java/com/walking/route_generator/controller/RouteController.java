package com.walking.route_generator.controller;

import com.walking.route_generator.dto.RouteGenerationRequestDto;
import com.walking.route_generator.model.Route;
import com.walking.route_generator.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // Получить все маршруты
    @GetMapping
    public List<Route> getAllRoutes() {
        return routeService.findAllRoutes();
    }

    // Удалить маршрут по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }

    // Генерация маршрута
    @PostMapping("/generate")
    public ResponseEntity<Route> generate(@Valid @RequestBody RouteGenerationRequestDto request) {
        Route route = routeService.generateRoute(request);
        return ResponseEntity.ok(route);
    }
}