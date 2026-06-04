// service/RoutingClient.java
package com.walking.route_generator.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.ArrayList;

@Component
public class RoutingClient {

    // Ключ лежит в application.properties:
    // ors.api.key=ваш_ключ_с_openrouteservice.org
    @Value("${ors.api.key}")
    private String apiKey;

    private static final String ORS_URL =
            "https://api.openrouteservice.org/v2/directions/foot-walking";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Возвращает реальное расстояние по дорогам между двумя точками (в км).
     * Если API недоступен — падбэк на Хаверсин * 1.4
     */
    public double getRoadDistanceKm(double fromLat, double fromLon,
                                    double toLat,   double toLon) {
        try {
            // ORS принимает координаты в формате [lon, lat] — именно в таком порядке!
            Map<String, Object> body = Map.of(
                    "coordinates", List.of(
                            List.of(fromLon, fromLat),
                            List.of(toLon,   toLat)
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(ORS_URL, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            // Расстояние приходит в метрах
            double meters = root
                    .path("routes").get(0)
                    .path("summary")
                    .path("distance")
                    .asDouble();

            return meters / 1000.0;

        } catch (Exception e) {
            // Fallback: Хаверсин с коэффициентом городских улиц
            System.err.println("ORS недоступен, используем Хаверсин: " + e.getMessage());
            return haversineFallback(fromLat, fromLon, toLat, toLon) * 1.4;
        }
    }

    /**
     * Возвращает encoded polyline всего маршрута через все waypoints.
     * Waypoints: [[lat, lon], ...] — первая и последняя точка это позиция пользователя.
     * Если ORS недоступен — возвращает null.
     */
    public String getRouteGeometry(List<List<Double>> waypoints) {
        try {
            List<List<Double>> orsCoords = new ArrayList<>();
            for (List<Double> wp : waypoints) {
                orsCoords.add(List.of(wp.get(1), wp.get(0))); // ORS ждёт [lon, lat]
            }

            Map<String, Object> body = Map.of("coordinates", orsCoords);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", apiKey);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response =
                    restTemplate.postForEntity(ORS_URL, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("routes").get(0).path("geometry").asText();

        } catch (Exception e) {
            System.err.println("ORS geometry недоступен: " + e.getMessage());
            return null;
        }
    }

    private double haversineFallback(double lat1, double lon1,
                                     double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}