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

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordHasher passwordHasher;

    public AuthService(UserRepository userRepository,
                         JwtService jwtService,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService     = jwtService;
        this.passwordHasher = PasswordHasher.from(passwordEncoder);
    }

    public AuthResponse login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing credentials");
        }

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordHasher.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(buildSpringUser(user));

        // FIX: incluir restaurante en la respuesta para que el frontend
        // (AuthContext.normalizeSession → data.restaurante) lo reciba correctamente.
        return new AuthResponse(
                token,
                user.getRole().name().toLowerCase(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRestaurant(),
                user.getTelefono()   // ← campo añadido
        );
    }

    public AuthResponse register(RegisterRequest request) {
        if (request == null || request.getEmail() == null
                || request.getPassword() == null || request.getNombre() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing registration fields");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        // Validar teléfono — debe ser exactamente 9 dígitos si se envía
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

        String token = jwtService.generateToken(buildSpringUser(saved));

        // FIX: incluir restaurante en la respuesta de registro también
        return new AuthResponse(
                token,
                saved.getRole().name().toLowerCase(),
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRestaurant(),
                saved.getTelefono()  // ← campo añadido
        );
    }

    private org.springframework.security.core.userdetails.UserDetails buildSpringUser(UserEntity user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()))
        );
    }
}