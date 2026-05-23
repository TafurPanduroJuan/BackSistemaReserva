package com.grupo6.Comanda.config;

import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.TableEntity;
import com.grupo6.Comanda.model.entities.UserEntity;
import com.grupo6.Comanda.model.enums.UserRole;
import com.grupo6.Comanda.repository.RestaurantRepository;
import com.grupo6.Comanda.repository.TableRepository;
import com.grupo6.Comanda.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

/**
 * Inicializa la base de datos con datos de prueba si está vacía.
 *
 * Usuarios creados por defecto:
 *   admin@comanda.com  / admin123   → rol: ADMINISTRADOR
 *   personal@comanda.com / admin123 → rol: PERSONAL
 *   usuario@comanda.com / admin123  → rol: USUARIO
 *
 * Restaurante de prueba: "La Bella Italia" (id=1) con 5 mesas
 */
@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo,
                                   RestaurantRepository restaurantRepo,
                                   TableRepository tableRepo,
                                   PasswordEncoder encoder) {
        return args -> {

            // ── Usuarios de prueba ─────────────────────────────────────────
            if (userRepo.count() == 0) {
                log.info("Creando usuarios de prueba...");

                userRepo.save(buildUser("Administrador", "admin@comanda.com",
                        "admin123", UserRole.ADMINISTRADOR, null, encoder));

                userRepo.save(buildUser("Personal Demo", "personal@comanda.com",
                        "admin123", UserRole.PERSONAL, "La Bella Italia", encoder));

                userRepo.save(buildUser("Usuario Demo", "usuario@comanda.com",
                        "admin123", UserRole.USUARIO, null, encoder));

                log.info("Usuarios creados: admin@comanda.com / admin123");
            }

            // ── Restaurante de prueba ──────────────────────────────────────
            if (restaurantRepo.count() == 0) {
                log.info("Creando restaurante de prueba...");

                RestaurantEntity rest = new RestaurantEntity();
                rest.setNombre("La Bella Italia");
                rest.setTipo("Italiana");
                rest.setDistrito("Miraflores");
                rest.setDireccion("Av. Larco 345");
                rest.setMensajePersonalizado("Bienvenidos a La Bella Italia, donde la pasta es artesanal.");
                rest.setMesas(5);
                rest.setTelefono("01-234-5678");
                rest.setEmail("reservas@labellaitalia.pe");
                rest.setImagen(null);
                rest.setHorarioApertura("12:00");
                rest.setHorarioCierre("23:00");
                RestaurantEntity saved = restaurantRepo.save(rest);

                // Crear 5 mesas para el restaurante
                String[] zonas = {"Terraza", "Terraza", "Salón Interior", "Salón Interior", "VIP"};
                int[] capacidades = {2, 4, 4, 6, 8};

                for (int i = 0; i < 5; i++) {
                    TableEntity mesa = new TableEntity();
                    mesa.setRestaurant(saved);
                    mesa.setNumero(i + 1);
                    mesa.setCapacidad(capacidades[i]);
                    mesa.setEstado("disponible");
                    mesa.setZona(zonas[i]);
                    tableRepo.save(mesa);
                }

                log.info("Restaurante 'La Bella Italia' creado con 5 mesas (id={})", saved.getId());
            }
        };
    }

    private UserEntity buildUser(String nombre, String email, String password,
                                  UserRole role, String restaurante, PasswordEncoder encoder) {
        UserEntity u = new UserEntity();
        u.setName(nombre);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(password));
        u.setRole(role);
        u.setRestaurant(restaurante);
        u.setAvatar(null);
        u.setCreatedAt(LocalDate.now().toString());
        return u;
    }
}
