package com.grupo6.Comanda.config;

import com.grupo6.Comanda.model.entities.UserEntity;
import com.grupo6.Comanda.model.enums.UserRole;
import com.grupo6.Comanda.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

/**
 * Crea el usuario administrador si la BD está vacía.
 * Solo se ejecuta una vez (cuando no hay ningún usuario).
 *
 * Credenciales: admin@comanda.com / admin123
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            if (userRepo.count() == 0) {
                UserEntity admin = new UserEntity();
                admin.setName("Administrador");
                admin.setEmail("admin@comanda.com");
                admin.setPasswordHash(encoder.encode("admin123"));
                admin.setRole(UserRole.ADMINISTRADOR);
                admin.setRestaurant(null);
                admin.setAvatar(null);
                admin.setCreatedAt(LocalDate.now().toString());
                userRepo.save(admin);

                log.info("=================================================");
                log.info("  Admin creado → admin@comanda.com / admin123");
                log.info("=================================================");
            }
        };
    }
}