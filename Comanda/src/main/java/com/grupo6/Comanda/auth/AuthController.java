package com.grupo6.Comanda.auth;


import com.grupo6.Comanda.auth.dto.AuthDtos.AuthResponse;
import com.grupo6.Comanda.auth.dto.AuthDtos.LoginRequest;
import com.grupo6.Comanda.auth.dto.AuthDtos.RegisterRequest;
import com.grupo6.Comanda.auth.dto.AuthDtos.GoogleAuthRequest;
import com.grupo6.Comanda.auth.dto.AuthDtos.ForgotPasswordRequest;
import com.grupo6.Comanda.auth.dto.AuthDtos.ResetPasswordRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService            authService;
    private final PasswordRecoveryService passwordRecoveryService;

    public AuthController(AuthService authService,
                          PasswordRecoveryService passwordRecoveryService) {
        this.authService             = authService;
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /** Login / registro con Google (idToken obtenido en el frontend) */
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleAuth(@RequestBody GoogleAuthRequest request) {
        return ResponseEntity.ok(authService.loginWithGoogle(request.getIdToken()));
    }

    /** Solicitar email de recuperación de contraseña */
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {
        passwordRecoveryService.requestReset(request.getEmail());
        // Respuesta genérica para no revelar si el email existe
        return ResponseEntity.ok(Map.of("message",
                "Si el correo está registrado recibirás un enlace en los próximos minutos."));
    }

    /** Confirmar nueva contraseña con el token del email */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestBody ResetPasswordRequest request) {
        passwordRecoveryService.confirmReset(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente."));
    }
}

