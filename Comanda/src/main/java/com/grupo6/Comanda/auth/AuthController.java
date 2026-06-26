package com.grupo6.Comanda.auth;


import com.grupo6.Comanda.auth.dto.AuthDtos.AuthResponse;
import com.grupo6.Comanda.auth.dto.AuthDtos.ForgotPasswordRequest;
import com.grupo6.Comanda.auth.dto.AuthDtos.LoginRequest;
import com.grupo6.Comanda.auth.dto.AuthDtos.RegisterRequest;
import com.grupo6.Comanda.model.entities.UserEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }
}

