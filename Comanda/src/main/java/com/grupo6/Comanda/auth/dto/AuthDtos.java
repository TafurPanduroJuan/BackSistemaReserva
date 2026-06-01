package com.grupo6.Comanda.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public final class AuthDtos {

    private AuthDtos() {}

    // ─────────────────────────────────────────────────────────────────────────
    // Login
    // ─────────────────────────────────────────────────────────────────────────

    @Schema(description = "Credenciales para iniciar sesión")
    public static class LoginRequest {

        @Schema(description = "Correo electrónico registrado", example = "diego@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        public String email;

        @Schema(description = "Contraseña del usuario", example = "Mi$Clave123", requiredMode = Schema.RequiredMode.REQUIRED)
        public String password;

        public LoginRequest() {}
        public String getEmail()    { return email; }
        public void   setEmail(String email)       { this.email = email; }
        public String getPassword() { return password; }
        public void   setPassword(String password) { this.password = password; }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Register
    // ─────────────────────────────────────────────────────────────────────────

    @Schema(description = "Datos para crear una cuenta nueva")
    public static class RegisterRequest {

        @Schema(description = "Nombre completo del usuario", example = "Diego García", requiredMode = Schema.RequiredMode.REQUIRED)
        public String nombre;

        @Schema(description = "Correo electrónico único", example = "diego@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        public String email;

        @Schema(description = "Contraseña (mínimo 6 caracteres)", example = "Mi$Clave123", requiredMode = Schema.RequiredMode.REQUIRED)
        public String password;

        @Schema(description = "Rol del usuario: administrador | personal | usuario", example = "usuario")
        public String rol;

        @Schema(description = "Nombre del restaurante asignado (solo para rol 'personal')", example = "La Bella Italia")
        public String restaurante;

        @Schema(description = "Teléfono de contacto (exactamente 9 dígitos)", example = "987654321")
        public Long telefono;

        public RegisterRequest() {}
        public String getNombre()     { return nombre; }
        public void   setNombre(String nombre)         { this.nombre = nombre; }
        public String getEmail()      { return email; }
        public void   setEmail(String email)           { this.email = email; }
        public String getPassword()   { return password; }
        public void   setPassword(String password)     { this.password = password; }
        public String getRol()        { return rol; }
        public void   setRol(String rol)               { this.rol = rol; }
        public String getRestaurante(){ return restaurante; }
        public void   setRestaurante(String restaurante){ this.restaurante = restaurante; }
        public Long   getTelefono()   { return telefono; }
        public void   setTelefono(Long telefono)       { this.telefono = telefono; }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UpdateMe — DTO dedicado para PUT /api/users/me
    // Swagger mostrará exactamente estos campos como ejemplo 
    // ─────────────────────────────────────────────────────────────────────────

    @Schema(description = "Campos que el usuario puede actualizar en su perfil")
    public static class UpdateMeRequest {

        @Schema(description = "Nuevo nombre del usuario (opcional)", example = "Diego García")
        public String nombre;

        @Schema(description = "URL del nuevo avatar (opcional)", example = "https://storage.example.com/avatars/diego.jpg")
        public String avatar;

        @Schema(description = "Nuevo teléfono (exactamente 9 dígitos, opcional)", example = "987654321")
        public Long telefono;

        public UpdateMeRequest() {}
        public String getNombre()   { return nombre; }
        public void   setNombre(String nombre)     { this.nombre = nombre; }
        public String getAvatar()   { return avatar; }
        public void   setAvatar(String avatar)     { this.avatar = avatar; }
        public Long   getTelefono() { return telefono; }
        public void   setTelefono(Long telefono)   { this.telefono = telefono; }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Response
    // ─────────────────────────────────────────────────────────────────────────

    @Schema(description = "Respuesta de autenticación con JWT")
    public static class AuthResponse {

        @Schema(description = "JWT para usar en Authorization: Bearer <token>", example = "eyJhbGciOiJIUzI1NiJ9...")
        public String token;

        @Schema(description = "Rol del usuario autenticado", example = "usuario")
        public String rol;

        @Schema(description = "ID del usuario", example = "1")
        public Long id;

        @Schema(description = "Nombre del usuario", example = "Diego García")
        public String nombre;

        @Schema(description = "Correo del usuario", example = "diego@example.com")
        public String email;

        public AuthResponse() {}

        public AuthResponse(String token, String rol) {
            this.token = token;
            this.rol   = rol;
        }

        public AuthResponse(String token, String rol, Long id, String nombre, String email) {
            this.token  = token;
            this.rol    = rol;
            this.id     = id;
            this.nombre = nombre;
            this.email  = email;
        }

        public String getToken()  { return token; }
        public void   setToken(String token)   { this.token = token; }
        public String getRol()    { return rol; }
        public void   setRol(String rol)       { this.rol = rol; }
        public Long   getId()     { return id; }
        public void   setId(Long id)           { this.id = id; }
        public String getNombre() { return nombre; }
        public void   setNombre(String nombre) { this.nombre = nombre; }
        public String getEmail()  { return email; }
        public void   setEmail(String email)   { this.email = email; }
    }
}