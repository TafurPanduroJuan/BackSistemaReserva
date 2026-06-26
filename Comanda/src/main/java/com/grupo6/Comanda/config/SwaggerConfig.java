package com.grupo6.Comanda.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger / OpenAPI 3.
 *
 * // Swagger UI disponible en: http://localhost:8080/comanda/api-docs
 * // JSON spec en:             http://localhost:8080/comanda/api-docs/json
 *
 * Para endpoints protegidos:
 *   1. Llama a POST /api/auth/login → copia el "token" de la respuesta
 *   2. Clic en "Authorize" → pega el token (sin escribir "Bearer ", Swagger lo agrega)
 *   3. Todos los requests siguientes incluirán el header Authorization: Bearer <token>
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Comanda API — Sistema de Reservas")
                        .description("""
                                Backend del Sistema de Reservas de Restaurantes — Grupo 6.
                                
                                **Flujo de autenticación:**
                                1. `POST /api/auth/register` para crear una cuenta, o
                                2. `POST /api/auth/login` para iniciar sesión.
                                3. Copia el campo `token` de la respuesta.
                                4. Haz clic en **Authorize** (arriba a la derecha) y pega el token.
                                5. Todos los endpoints protegidos funcionarán automáticamente.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Grupo 6")
                                .email("grupo6@example.com")))

                // Registra el esquema Bearer JWT
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Pega aquí el token JWT obtenido en /api/auth/login")))

                // Aplica JWT a todos los endpoints por defecto
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                // Tags ordenados para la UI de Swagger
                .tags(List.of(
                        new Tag().name("Auth").description("Registro e inicio de sesión"),
                        new Tag().name("Restaurants").description("Gestión de restaurantes"),
                        new Tag().name("Tables").description("Mesas y disponibilidad"),
                        new Tag().name("Reservations").description("Reservas de clientes"),
                        new Tag().name("Comments").description("Comentarios y feedback"),
                        new Tag().name("Users").description("Gestión de usuarios (admin)")
                ));
    }
}
