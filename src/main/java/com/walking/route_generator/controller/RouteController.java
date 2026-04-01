package com.walking.route_generator.controller;

import com.walking.route_generator.model.Route;
import com.walking.route_generator.service.RouteService;
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
    //Получаем короче все маршруты
    @GetMapping
    public List<Route> getAllRoutes(){
        return routeService.findAllRoutes();
    }

    //Удалить маршрут по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id){
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }


     //тестовый маршрут-заглушка
    @PostMapping("/stub")
    public ResponseEntity<Route> createStub() {
        Route created = routeService.createStubRoute();
        return ResponseEntity.ok(created);
    }

    //Заготовка для генерации маршрута
    @PostMapping("/generate")
    public Route generate(@RequestParam double distance, @RequestParam String tempo, @RequestParam boolean circular) {
        return routeService.generateRoute(distance, tempo, circular);
    }

}
