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

   
}
