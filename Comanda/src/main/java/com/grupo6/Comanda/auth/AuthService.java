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
        // Return lowercase rol to match frontend expectations (administrador, personal, usuario)
        return new AuthResponse(token, user.getRole().name().toLowerCase());
    }

    public AuthResponse register(RegisterRequest request) {
        if (request == null || request.getEmail() == null
                || request.getPassword() == null || request.getNombre() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing registration fields");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        // Frontend sends rol as lowercase ("administrador", "personal", "usuario")
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
        user.setAvatar(null);
        user.setCreatedAt(LocalDate.now().toString());

        UserEntity saved = userRepository.save(user);

        String token = jwtService.generateToken(buildSpringUser(saved));
        return new AuthResponse(token, saved.getRole().name().toLowerCase());
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