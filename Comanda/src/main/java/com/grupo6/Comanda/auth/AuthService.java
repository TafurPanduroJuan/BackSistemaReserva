package com.grupo6.Comanda.auth;

import com.grupo6.Comanda.auth.dto.AuthDtos.AuthResponse;
import com.grupo6.Comanda.auth.dto.AuthDtos.LoginRequest;
import com.grupo6.Comanda.auth.dto.AuthDtos.RegisterRequest;

import com.grupo6.Comanda.model.entities.UserEntity;
import com.grupo6.Comanda.model.enums.UserRole;
import com.grupo6.Comanda.repository.UserRepository;
import com.grupo6.Comanda.security.PasswordHasher;
import com.grupo6.Comanda.security.jwt.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository       userRepository;
    private final JwtService           jwtService;
    private final PasswordHasher       passwordHasher;
    private final GoogleTokenVerifier  googleVerifier;

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       GoogleTokenVerifier googleVerifier) {
        this.userRepository = userRepository;
        this.jwtService     = jwtService;
        this.passwordHasher = PasswordHasher.from(passwordEncoder);
        this.googleVerifier = googleVerifier;
    }

    // ── Login con email/password ──────────────────────────────────────────────
    public AuthResponse login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing credentials");
        }

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordHasher.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return buildResponse(user);
    }

    // ── Registro con email/password ───────────────────────────────────────────
    public AuthResponse register(RegisterRequest request) {
        if (request == null || request.getEmail() == null
                || request.getPassword() == null || request.getNombre() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing registration fields");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        if (request.getTelefono() != null) {
            String telStr = String.valueOf(request.getTelefono());
            if (!telStr.matches("\\d{9}")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El teléfono debe tener exactamente 9 dígitos numéricos");
            }
        }

        UserRole role;
        try {
            String rolRaw = request.getRol();
            role = (rolRaw != null)
                    ? UserRole.valueOf(rolRaw.toUpperCase())
                    : UserRole.USUARIO;
        } catch (IllegalArgumentException ex) {
            role = UserRole.USUARIO;
        }

        UserEntity user = new UserEntity();
        user.setName(request.getNombre());
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setPasswordHash(passwordHasher.hash(request.getPassword()));
        user.setRestaurant(request.getRestaurante());
        user.setTelefono(request.getTelefono());
        user.setAvatar(null);
        user.setCreatedAt(LocalDate.now().toString());

        UserEntity saved = userRepository.save(user);
        return buildResponse(saved);
    }

    // ── Login / Registro con Google ───────────────────────────────────────────
    /**
     * Verifica el Google ID Token, luego:
     *   - Si ya existe un usuario con ese google_email → login directo.
     *   - Si existe un usuario con ese email → vincula google_email y hace login.
     *   - Si no existe → crea cuenta nueva con rol USUARIO.
     */
    public AuthResponse loginWithGoogle(String idToken) {
        if (idToken == null || idToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requiere el ID token de Google");
        }

        GoogleTokenVerifier.GoogleUserInfo info;
        try {
            info = googleVerifier.verify(idToken);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        // 1. Buscar por google_email ya vinculado
        Optional<UserEntity> byGoogle = userRepository.findByGoogleEmail(info.email());
        if (byGoogle.isPresent()) {
            return buildResponse(byGoogle.get());
        }

        // 2. Buscar por email principal (vincular automáticamente)
        Optional<UserEntity> byEmail = userRepository.findByEmail(info.email());
        if (byEmail.isPresent()) {
            UserEntity user = byEmail.get();
            user.setGoogleEmail(info.email());
            if (user.getAvatar() == null && info.picture() != null) {
                user.setAvatar(info.picture());
            }
            userRepository.save(user);
            return buildResponse(user);
        }

        // 3. Crear cuenta nueva
        UserEntity newUser = new UserEntity();
        newUser.setName(info.name());
        newUser.setEmail(info.email());
        newUser.setGoogleEmail(info.email());
        newUser.setRole(UserRole.USUARIO);
        // Contraseña aleatoria (no se usa para login con Google)
        newUser.setPasswordHash(passwordHasher.hash(UUID.randomUUID().toString()));
        newUser.setAvatar(info.picture());
        newUser.setCreatedAt(LocalDate.now().toString());

        UserEntity saved = userRepository.save(newUser);
        return buildResponse(saved);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private AuthResponse buildResponse(UserEntity user) {
        String token = jwtService.generateToken(buildSpringUser(user));
        AuthResponse r = new AuthResponse(
                token,
                user.getRole().name().toLowerCase(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRestaurant(),
                user.getTelefono(),
                user.getGoogleEmail()
        );
        return r;
    }

    private org.springframework.security.core.userdetails.UserDetails buildSpringUser(UserEntity user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash() != null ? user.getPasswordHash() : "",
                List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()))
        );
    }
}
