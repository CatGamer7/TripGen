package com.walking.route_generator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50)
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, message = "Пароль должен быть не менее 8 символов")
    @Column(nullable = false)
    private String password; // В будущем здесь будет хэш пароля

    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email обязателен")
    @Column(unique = true, nullable = false)
    private String email;

    private String role = "ROLE_USER"; // По умолчанию обычный пользователь

    // Связь:
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Route> savedRoutes = new ArrayList<>();

    public User() {}

    //Getter and Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Route> getSavedRoutes() { return savedRoutes; }
    public void setSavedRoutes(List<Route> savedRoutes) { this.savedRoutes = savedRoutes; }

}
