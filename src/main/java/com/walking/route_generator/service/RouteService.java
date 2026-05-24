// service/RouteService.java
package com.walking.route_generator.service;

import com.walking.route_generator.dto.RouteGenerationRequestDto;
import com.walking.route_generator.model.*;
import com.walking.route_generator.repository.PointRepository;
import com.walking.route_generator.repository.RouteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final PointRepository pointRepository;
    private final RoutingClient routingClient;

    public RouteService(RouteRepository routeRepository, PointRepository pointRepository, RoutingClient routingClient) {
        this.routeRepository = routeRepository;
        this.pointRepository = pointRepository;
        this.routingClient   = routingClient;
    }

    public List<Route> findAllRoutes(){
        return routeRepository.findAll();
    }
    public Optional<Route> findRouteById(Long id) {
        return routeRepository.findById(id);
    }
    public void deleteRoute(Long id){
        routeRepository.deleteById(id);
    }



    public Route generateRoute(RouteGenerationRequestDto req) {

        List<Point> candidates = pointRepository.findByMood(req.getMood());
        if (candidates.size() < 2) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Недостаточно точек с настроением: " + req.getMood());
        }


        Point startPoint = findNearestByHaversine(
                req.getUserLatitude(), req.getUserLongitude(), candidates
        );


        List<Point> chain = buildCircularChain(
                startPoint,
                candidates,
                req.getUserLatitude(),
                req.getUserLongitude(),
                req.getDesiredDistanceKm()
        );


        double totalDistance = calcRoadDistance(
                chain,
                req.getUserLatitude(),
                req.getUserLongitude()
        );


        Tempo tempo = parseTempo(req.getTempo());
        int estimatedMinutes = (int) Math.round(
                (totalDistance / tempo.getKmPerHour()) * 60
        );


        Route route = new Route();
        route.setMood(req.getMood());
        route.setPoints(chain);
        route.setTotalDistance(Math.round(totalDistance * 100.0) / 100.0);
        route.setTempo(tempo.name());
        route.setEstimatedMinutes(estimatedMinutes);
        route.setTitle("Кольцевой · " + req.getMood().getRussianName()
                + " · " + tempo.name());

        return routeRepository.save(route);
    }


    private List<Point> buildCircularChain(Point startPoint,
                                           List<Point> allCandidates,
                                           double userLat, double userLon,
                                           double desiredKm) {
        List<Point> remaining = new ArrayList<>(allCandidates);
        remaining.remove(startPoint);

        List<Point> chain = new ArrayList<>();
        chain.add(startPoint);

        double accumulated = 0.0;
        // Бюджет на "уходящую" часть — примерно половина желаемой дистанции
        // Оставшееся уйдёт на возврат к пользователю
        double outboundBudget = desiredKm * 0.5;

        Point current = startPoint;

        //набираем точки, пока не потратили половину бюджета
        while (!remaining.isEmpty()) {
            Point nearest = findNearestByHaversine(
                    current.getLatitude(), current.getLongitude(), remaining
            );
            double seg = routingClient.getRoadDistanceKm(
                    current.getLatitude(), current.getLongitude(),
                    nearest.getLatitude(), nearest.getLongitude()
            );

            if (accumulated + seg > outboundBudget * 1.2) {
                break; // 20% допуск на outbound
            }

            remaining.remove(nearest);
            chain.add(nearest);
            accumulated += seg;
            current = nearest;

            if (accumulated >= outboundBudget * 0.85) {
                break; // Набрали достаточно — переходим к возврату
            }
        }


        // Пытаемся захватить ещё точки на пути домой
        double returnBudget = desiredKm - accumulated;
        if (!remaining.isEmpty()) {
            addReturnPoints(chain, remaining, current,
                    userLat, userLon, returnBudget);
        }

        return chain;
    }


    private void addReturnPoints(List<Point> chain,
                                 List<Point> remaining,
                                 Point current,
                                 double userLat, double userLon,
                                 double returnBudget) {
        double accumulated = 0.0;

        while (!remaining.isEmpty() && accumulated < returnBudget * 0.8) {
            // Ищем точку, которая ближе к пути "current → user"
            Point bestOnWay = findPointOnWayHome(
                    current, remaining, userLat, userLon
            );
            if (bestOnWay == null) break;

            double segToPoint = routingClient.getRoadDistanceKm(
                    current.getLatitude(), current.getLongitude(),
                    bestOnWay.getLatitude(), bestOnWay.getLongitude()
            );
            double segToUser = routingClient.getRoadDistanceKm(
                    bestOnWay.getLatitude(), bestOnWay.getLongitude(),
                    userLat, userLon
            );

            if (accumulated + segToPoint + segToUser > returnBudget * 1.2) {
                break;
            }

            remaining.remove(bestOnWay);
            chain.add(bestOnWay);
            accumulated += segToPoint;
            current = bestOnWay;
        }
    }


    private Point findPointOnWayHome(Point current,
                                     List<Point> candidates,
                                     double userLat, double userLon) {
        double directToUser = haversineKm(
                current.getLatitude(), current.getLongitude(), userLat, userLon
        );

        Point best = null;
        double bestDetour = Double.MAX_VALUE;

        for (Point candidate : candidates) {
            double toCandidate = haversineKm(
                    current.getLatitude(), current.getLongitude(),
                    candidate.getLatitude(), candidate.getLongitude()
            );
            double toUser = haversineKm(
                    candidate.getLatitude(), candidate.getLongitude(),
                    userLat, userLon
            );

            double detourRatio = (toCandidate + toUser) / (directToUser + 0.001);


            if (detourRatio < 1.3 && detourRatio < bestDetour) {
                bestDetour = detourRatio;
                best = candidate;
            }
        }

        return best;
    }


    private double calcRoadDistance(List<Point> chain,
                                    double userLat, double userLon) {
        if (chain.isEmpty()) return 0.0;

        double total = 0.0;

        // От пользователя до первой точки
        total += routingClient.getRoadDistanceKm(
                userLat, userLon,
                chain.get(0).getLatitude(), chain.get(0).getLongitude()
        );

        // Между точками
        for (int i = 0; i < chain.size() - 1; i++) {
            total += routingClient.getRoadDistanceKm(
                    chain.get(i).getLatitude(),     chain.get(i).getLongitude(),
                    chain.get(i + 1).getLatitude(), chain.get(i + 1).getLongitude()
            );
        }

        // От последней точки обратно к пользователю
        total += routingClient.getRoadDistanceKm(
                chain.get(chain.size() - 1).getLatitude(),
                chain.get(chain.size() - 1).getLongitude(),
                userLat, userLon
        );

        return total;
    }


    private Point findNearestByHaversine(double lat, double lon,
                                         List<Point> candidates) {
        Point nearest = null;
        double minDist = Double.MAX_VALUE;
        for (Point p : candidates) {
            double d = haversineKm(lat, lon, p.getLatitude(), p.getLongitude());
            if (d < minDist) { minDist = d; nearest = p; }
        }
        return nearest;
    }

    private double haversineKm(double lat1, double lon1,
                               double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private Tempo parseTempo(String s) {
        try {
            return Tempo.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Неверный темп. Допустимо: SLOW, NORMAL, FAST");
        }
    }
}