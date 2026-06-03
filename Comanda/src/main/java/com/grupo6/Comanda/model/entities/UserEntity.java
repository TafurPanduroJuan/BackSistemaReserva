package com.grupo6.Comanda.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.grupo6.Comanda.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Schema(description = "Datos de un usuario registrado en la plataforma")
@Entity
@Table(name = "users")
public class UserEntity {

    @Schema(description = "ID interno del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Diego García")
    @Column(name = "nombre", nullable = false)
    private String name;

    @Schema(description = "Correo electrónico único", example = "diego@example.com")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Schema(description = "Rol del usuario", example = "usuario")
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private UserRole role;

    @Schema(description = "Restaurante asignado (solo para rol PERSONAL)", example = "La Bella Italia")
    @Column(name = "restaurante")
    private String restaurant;

    @Schema(description = "URL del avatar del usuario")
    @Column(name = "avatar")
    private String avatar;

    @Schema(description = "Teléfono de contacto (exactamente 9 dígitos)", example = "987654321")
    @Column(name = "telefono")
    private Long telefono;

    @Schema(description = "Fecha de registro (ISO-8601)", example = "2026-06-01", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "fecha_registro", nullable = false)
    private String createdAt;

    public Long   getId()           { return id; }
    public void   setId(Long id)    { this.id = id; }

    // nombre — serialized as "nombre" for the React frontend
    @JsonProperty("nombre")
    public String getName()         { return name; }
    public void   setName(String name) { this.name = name; }

    public String getEmail()        { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void   setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    // rol — serialized as "rol" (lowercase) for the React frontend
    @JsonProperty("rol")
    public String getRolAsString()  { return role != null ? role.name().toLowerCase() : null; }

    public UserRole getRole()       { return role; }
    public void     setRole(UserRole role) { this.role = role; }

    // restaurante — serialized as "restaurante" for the React frontend
    @JsonProperty("restaurante")
    public String getRestaurant()   { return restaurant; }
    public void   setRestaurant(String restaurant) { this.restaurant = restaurant; }

    public String getAvatar()       { return avatar; }
    public void   setAvatar(String avatar) { this.avatar = avatar; }

    public Long   getTelefono()     { return telefono; }
    public void   setTelefono(Long telefono) { this.telefono = telefono; }

    @JsonProperty("fechaRegistro")
    public String getCreatedAt()    { return createdAt; }
    public void   setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
