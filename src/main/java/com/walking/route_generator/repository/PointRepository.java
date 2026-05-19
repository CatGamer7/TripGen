package com.walking.route_generator.repository;

import com.walking.route_generator.model.Mood;
import com.walking.route_generator.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByMood(Mood mood);
}
