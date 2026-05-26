package com.walking.route_generator.service;

import com.walking.route_generator.dto.UserRegisrtationDto;
import com.walking.route_generator.model.Point;
import com.walking.route_generator.model.User;
import com.walking.route_generator.repository.PointRepository;
import com.walking.route_generator.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PointRepository pointRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, PointRepository pointRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pointRepository = pointRepository;
    }

    @Transactional
    public void registerUser(UserRegisrtationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Имя пользователя уже занято");
        }
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email уже зарегистрирован");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());

        String encodedPassword = passwordEncoder.encode(registrationDto.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    public void addPointToFavorite(Long userId, Long pointId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Точка не найдена"));

        user.getFavoritePoints().add(point);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

}