package com.grupo6.Comanda.auth;

import com.grupo6.Comanda.auth.dto.AuthDtos.AuthResponse;
import com.grupo6.Comanda.auth.dto.AuthDtos.LoginRequest;
import com.grupo6.Comanda.model.entities.UserEntity;
import com.grupo6.Comanda.model.enums.UserRole;
import com.grupo6.Comanda.repository.UserRepository;
import com.grupo6.Comanda.security.jwt.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del proceso de LOGIN (AuthService#login).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - login")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GoogleTokenVerifier googleVerifier;

    private AuthService authService;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        // AuthService envuelve el PasswordEncoder real dentro de un PasswordHasher,
        // por lo que se inyecta el mock de PasswordEncoder directamente al constructor.
        authService = new AuthService(userRepository, jwtService, passwordEncoder, googleVerifier);

        user = new UserEntity();
        user.setId(1L);
        user.setName("Diego García");
        user.setEmail("diego@example.com");
        user.setPasswordHash("$2a$10$hashDeContrasenaSimulado");
        user.setRole(UserRole.USUARIO);
        user.setCreatedAt("2026-01-15");
    }

    @Test
    @DisplayName("Login exitoso: credenciales válidas devuelven token y datos del usuario")
    void login_exitoso_conCredencialesValidas() {
        LoginRequest request = new LoginRequest();
        request.setEmail("diego@example.com");
        request.setPassword("Mi$Clave123");

        when(userRepository.findByEmail("diego@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Mi$Clave123", user.getPasswordHash())).thenReturn(true);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("token.jwt.simulado");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("token.jwt.simulado", response.getToken());
        assertEquals("usuario", response.getRol());
        assertEquals(1L, response.getId());
        assertEquals("Diego García", response.getNombre());
        assertEquals("diego@example.com", response.getEmail());

        verify(userRepository).findByEmail("diego@example.com");
        verify(passwordEncoder).matches("Mi$Clave123", user.getPasswordHash());
        verify(jwtService).generateToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("Login falla con 401 cuando el email no está registrado")
    void login_falla_usuarioNoExiste() {
        LoginRequest request = new LoginRequest();
        request.setEmail("noexiste@example.com");
        request.setPassword("cualquierClave");

        when(userRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.login(request));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Login falla con 401 cuando la contraseña es incorrecta")
    void login_falla_contrasenaIncorrecta() {
        LoginRequest request = new LoginRequest();
        request.setEmail("diego@example.com");
        request.setPassword("claveIncorrecta");

        when(userRepository.findByEmail("diego@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("claveIncorrecta", user.getPasswordHash())).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.login(request));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Login falla con 400 cuando el email es nulo")
    void login_falla_emailNulo() {
        LoginRequest request = new LoginRequest();
        request.setEmail(null);
        request.setPassword("cualquierClave");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.login(request));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Login falla con 400 cuando la contraseña es nula")
    void login_falla_passwordNulo() {
        LoginRequest request = new LoginRequest();
        request.setEmail("diego@example.com");
        request.setPassword(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.login(request));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Login falla con 400 cuando el request completo es nulo")
    void login_falla_requestNulo() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.login(null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verifyNoInteractions(userRepository);
    }
}