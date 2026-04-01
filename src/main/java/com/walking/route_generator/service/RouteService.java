package com.walking.route_generator.service;


import com.walking.route_generator.model.Route;
import com.walking.route_generator.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }
    //Получаем(читаем) все маршруты из бд
    public List<Route> findAllRoutes(){
        return routeRepository.findAll();
    }

    //Находим конкретный маршрут по Id
    public Optional<Route> findRouteById(Long id) {
        return routeRepository.findById(id);
    }

    //Удаляем маршрут
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }

    public Route createStubRoute() {

        Route stub = new Route();

        //Test
        stub.setTitle("Тестовый маршрут для проверки БД");
        stub.setTotalDistance(2.5);
        stub.setTempo("Прогулочный");

        return routeRepository.save(stub);
    }


    //Метод для генерации маршрута(Будущий алгоритм)
    public Route generateRoute(double length, String tempo, boolean circular) {
        // Здесь в будущем будет логика подбора точек маршрута(Наверное)

        Route generatedRoute = new Route();
        generatedRoute.setTotalDistance(length);
        generatedRoute.setTempo(tempo);

        return routeRepository.save(generatedRoute);
    }
}
