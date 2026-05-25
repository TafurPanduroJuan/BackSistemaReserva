package com.grupo6.Comanda.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Envuelve PasswordEncoder para que AuthService no dependa
 * directamente de Spring Security.
 */

public final class PasswordHasher {

    private final PasswordEncoder encoder;

    private PasswordHasher(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public static PasswordHasher from(PasswordEncoder encoder) {
        return new PasswordHasher(encoder);
    }

    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}