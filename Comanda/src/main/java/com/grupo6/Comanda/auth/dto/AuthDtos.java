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
    // Google OAuth
    // ─────────────────────────────────────────────────────────────────────────

    @Schema(description = "Token de Google ID para autenticación con Google")
    public static class GoogleAuthRequest {
        @Schema(description = "ID Token devuelto por Google Sign-In", requiredMode = Schema.RequiredMode.REQUIRED)
        public String idToken;

        public GoogleAuthRequest() {}
        public String getIdToken() { return idToken; }
        public void   setIdToken(String t) { this.idToken = t; }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Recuperación de contraseña
    // ─────────────────────────────────────────────────────────────────────────

    @Schema(description = "Solicitud de recuperación: email de la cuenta o email de Google vinculado")
    public static class ForgotPasswordRequest {
        @Schema(description = "Correo de la cuenta Comanda o correo Google vinculado", requiredMode = Schema.RequiredMode.REQUIRED)
        public String email;

        public ForgotPasswordRequest() {}
        public String getEmail() { return email; }
        public void   setEmail(String e) { this.email = e; }
    }

    @Schema(description = "Restablece la contraseña usando el token recibido por email")
    public static class ResetPasswordRequest {
        @Schema(description = "Token recibido en el correo", requiredMode = Schema.RequiredMode.REQUIRED)
        public String token;
        @Schema(description = "Nueva contraseña (mínimo 6 caracteres)", requiredMode = Schema.RequiredMode.REQUIRED)
        public String newPassword;

        public ResetPasswordRequest() {}
        public String getToken()       { return token; }
        public void   setToken(String t)            { this.token = t; }
        public String getNewPassword() { return newPassword; }
        public void   setNewPassword(String p)      { this.newPassword = p; }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UpdateMe — DTO dedicado para PUT /api/users/me
    // ─────────────────────────────────────────────────────────────────────────

    @Schema(description = "Campos que el usuario puede actualizar en su perfil")
    public static class UpdateMeRequest {

        @Schema(description = "Nuevo nombre del usuario (opcional)", example = "Diego García")
        public String nombre;

        @Schema(description = "URL del nuevo avatar (opcional)", example = "https://storage.example.com/avatars/diego.jpg")
        public String avatar;

        @Schema(description = "Nuevo teléfono (exactamente 9 dígitos, opcional)", example = "987654321")
        public Long telefono;

        @Schema(description = "Correo de Google vinculado (para recuperación de contraseña)", example = "diego@gmail.com")
        public String googleEmail;

        public UpdateMeRequest() {}
        public String getNombre()   { return nombre; }
        public void   setNombre(String nombre)     { this.nombre = nombre; }
        public String getAvatar()   { return avatar; }
        public void   setAvatar(String avatar)     { this.avatar = avatar; }
        public Long   getTelefono() { return telefono; }
        public void   setTelefono(Long telefono)   { this.telefono = telefono; }
        public String getGoogleEmail() { return googleEmail; }
        public void   setGoogleEmail(String g) { this.googleEmail = g; }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // ChangeRole — DTO dedicado para PUT /api/users/{id}/role
    // ─────────────────────────────────────────────────────────────────────────

    @Schema(description = "Datos para cambiar el rol de un usuario")
    public static class ChangeRoleRequest {

        @Schema(
            description = "Nuevo rol del usuario",
            example = "personal",
            allowableValues = {"administrador", "personal", "usuario"},
            requiredMode = Schema.RequiredMode.REQUIRED
        )
        private String rol;

        @Schema(description = "Restaurante asignado (obligatorio si rol es 'personal')", example = "La Bella Italia")
        private String restaurante;

        public ChangeRoleRequest() {}
        public String getRol()         { return rol; }
        public void   setRol(String rol)               { this.rol = rol; }
        public String getRestaurante() { return restaurante; }
        public void   setRestaurante(String restaurante){ this.restaurante = restaurante; }
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
        @Schema(description = "Teléfono del usuario", example = "999123456")
        public Long telefono;

        // FIX: El frontend (AuthContext.normalizeSession) lee data.restaurante para
        // saber qué restaurante gestiona un usuario con rol PERSONAL.
        // Sin este campo, el contexto lo guarda como null y el personal no puede
        // filtrar sus reservas / mesas correctamente.
        @Schema(description = "Restaurante asignado (solo rol PERSONAL)", example = "La Bella Italia")
        public String restaurante;

        @Schema(description = "Correo de Google vinculado", example = "diego@gmail.com")
        public String googleEmail;

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

        // constructor completo
        public AuthResponse(String token, String rol, Long id, String nombre, String email, String restaurante, Long telefono) {
            this.token       = token;
            this.rol         = rol;
            this.id          = id;
            this.nombre      = nombre;
            this.email       = email;
            this.restaurante = restaurante;
            this.telefono    = telefono;
        }

        // constructor con googleEmail
        public AuthResponse(String token, String rol, Long id, String nombre, String email, String restaurante, Long telefono, String googleEmail) {
            this(token, rol, id, nombre, email, restaurante, telefono);
            this.googleEmail = googleEmail;
        }

        public String getToken()        { return token; }
        public void   setToken(String token)             { this.token = token; }
        public String getRol()          { return rol; }
        public void   setRol(String rol)                 { this.rol = rol; }
        public Long   getId()           { return id; }
        public void   setId(Long id)    { this.id = id; }
        public String getNombre()       { return nombre; }
        public void   setNombre(String nombre)           { this.nombre = nombre; }
        public String getEmail()        { return email; }
        public void   setEmail(String email)             { this.email = email; }
        public String getRestaurante()  { return restaurante; }
        public void   setRestaurante(String restaurante) { this.restaurante = restaurante; }
        public String getGoogleEmail()  { return googleEmail; }
        public void   setGoogleEmail(String g)           { this.googleEmail = g; }
    }
}