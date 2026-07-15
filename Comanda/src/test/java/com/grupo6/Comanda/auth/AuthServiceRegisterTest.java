package com.grupo6.Comanda.auth;

import com.grupo6.Comanda.auth.dto.AuthDtos.AuthResponse;
import com.grupo6.Comanda.auth.dto.AuthDtos.RegisterRequest;
import com.grupo6.Comanda.model.entities.UserEntity;
import com.grupo6.Comanda.model.enums.UserRole;
import com.grupo6.Comanda.repository.UserRepository;
import com.grupo6.Comanda.security.jwt.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del proceso de REGISTRO (AuthService#register).
 *
 * Igual que AuthServiceTest: se mockean UserRepository, JwtService y
 * PasswordEncoder para aislar la lógica de negocio de AuthService,
 * sin levantar Spring ni depender de una base de datos real.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - register")
class AuthServiceRegisterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GoogleTokenVerifier googleVerifier;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, jwtService, passwordEncoder, googleVerifier);
    }

    /** Simula que el repositorio asigna un ID al guardar, como haría la BD real. */
    private UserEntity simulateSave(UserEntity user) {
        user.setId(99L);
        return user;
    }

    @Test
    @DisplayName("Registro exitoso: crea el usuario con rol USUARIO por defecto y devuelve token")
    void register_exitoso_conDatosValidos() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Ana Torres");
        request.setEmail("ana@example.com");
        request.setPassword("Clave$123");
        // rol no especificado -> debe quedar como USUARIO

        when(userRepository.findByEmail("ana@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Clave$123")).thenReturn("$2a$10$hashSimulado");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> simulateSave(inv.getArgument(0)));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("token.jwt.simulado");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("token.jwt.simulado", response.getToken());
        assertEquals("usuario", response.getRol());
        assertEquals(99L, response.getId());
        assertEquals("Ana Torres", response.getNombre());
        assertEquals("ana@example.com", response.getEmail());

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        assertEquals(UserRole.USUARIO, captor.getValue().getRole());
        assertEquals("$2a$10$hashSimulado", captor.getValue().getPasswordHash());
    }

    @Test
    @DisplayName("Registro falla con 409 cuando el email ya está registrado")
    void register_falla_emailYaRegistrado() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Ana Torres");
        request.setEmail("ana@example.com");
        request.setPassword("Clave$123");

        when(userRepository.findByEmail("ana@example.com"))
                .thenReturn(Optional.of(new UserEntity()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.register(request));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Registro falla con 400 cuando falta el nombre")
    void register_falla_nombreNulo() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre(null);
        request.setEmail("ana@example.com");
        request.setPassword("Clave$123");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.register(request));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Registro falla con 400 cuando falta el email")
    void register_falla_emailNulo() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Ana Torres");
        request.setEmail(null);
        request.setPassword("Clave$123");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.register(request));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Registro falla con 400 cuando falta la contraseña")
    void register_falla_passwordNulo() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Ana Torres");
        request.setEmail("ana@example.com");
        request.setPassword(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.register(request));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Registro falla con 400 cuando el request completo es nulo")
    void register_falla_requestNulo() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.register(null));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Registro falla con 400 cuando el teléfono no tiene exactamente 9 dígitos")
    void register_falla_telefonoInvalido() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Ana Torres");
        request.setEmail("ana@example.com");
        request.setPassword("Clave$123");
        request.setTelefono(12345L); // solo 5 dígitos, debe ser exactamente 9

        when(userRepository.findByEmail("ana@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> authService.register(request));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Registro acepta un teléfono válido de 9 dígitos")
    void register_exitoso_conTelefonoValido() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Ana Torres");
        request.setEmail("ana@example.com");
        request.setPassword("Clave$123");
        request.setTelefono(987654321L);

        when(userRepository.findByEmail("ana@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Clave$123")).thenReturn("$2a$10$hashSimulado");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> simulateSave(inv.getArgument(0)));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("token.jwt.simulado");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        // AuthResponse.telefono es un campo público sin getter definido en la clase
        assertEquals(987654321L, response.telefono);
    }

    @Test
    @DisplayName("Registro con rol inválido/desconocido cae por defecto a USUARIO")
    void register_rolInvalido_defaultsAUsuario() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Ana Torres");
        request.setEmail("ana@example.com");
        request.setPassword("Clave$123");
        request.setRol("rol-que-no-existe");

        when(userRepository.findByEmail("ana@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Clave$123")).thenReturn("$2a$10$hashSimulado");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> simulateSave(inv.getArgument(0)));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("token.jwt.simulado");

        AuthResponse response = authService.register(request);

        assertEquals("usuario", response.getRol());
    }

    @Test
    @DisplayName("Registro con rol PERSONAL guarda el restaurante asignado")
    void register_conRolPersonal_guardaRestaurante() {
        RegisterRequest request = new RegisterRequest();
        request.setNombre("Carlos Mesa");
        request.setEmail("carlos@example.com");
        request.setPassword("Clave$123");
        request.setRol("personal");
        request.setRestaurante("La Bella Italia");

        when(userRepository.findByEmail("carlos@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Clave$123")).thenReturn("$2a$10$hashSimulado");
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> simulateSave(inv.getArgument(0)));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("token.jwt.simulado");

        AuthResponse response = authService.register(request);

        assertEquals("personal", response.getRol());
        assertEquals("La Bella Italia", response.getRestaurante());
    }
}