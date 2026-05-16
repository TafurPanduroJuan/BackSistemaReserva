package com.grupo6.Comanda.model.entities;

import com.grupo6.Comanda.model.enums.UserRole;
import jakarta.persistence.*;


@Entity
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // SQLite: INTEGER PRIMARY KEY auto-increments
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private UserRole role;

    @Column(name = "restaurante")
    private String restaurant;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "fecha_registro", nullable = false)
    private String createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getRestaurant() { return restaurant; }
    public void setRestaurant(String restaurant) { this.restaurant = restaurant; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
