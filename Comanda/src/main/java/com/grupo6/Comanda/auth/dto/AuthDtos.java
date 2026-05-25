package com.grupo6.Comanda.auth.dto;

public final class AuthDtos {

    private AuthDtos() {}

    public static class LoginRequest {
        public String email;
        public String password;

        public LoginRequest() {}
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        public String nombre;
        public String email;
        public String password;
        /** Frontend sends lowercase: administrador | personal | usuario */
        public String rol;
        public String restaurante;
        public String telefono;

        public RegisterRequest() {}
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRol() { return rol; }
        public void setRol(String rol) { this.rol = rol; }
        public String getRestaurante() { return restaurante; }
        public void setRestaurante(String restaurante) { this.restaurante = restaurante; }
        public String getTelefono() { return telefono; }
        public void setTelefono(String telefono) { this.telefono = telefono; }
    }

   
}
