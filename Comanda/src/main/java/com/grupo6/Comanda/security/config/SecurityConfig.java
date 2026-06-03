package com.grupo6.Comanda.security.config;

import com.grupo6.Comanda.security.service.CustomUserDetailsService;
import com.grupo6.Comanda.security.jwt.JwtAuthenticationFilter;
import com.grupo6.Comanda.security.jwt.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    // Lee los orígenes permitidos desde application.properties
    @Value("${comanda.cors.allowed-origins:http://localhost:5173}")
    private String allowedOriginsRaw;

    public SecurityConfig(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS global — debe registrarse aquí (no en application.properties)
     * porque Spring Security intercepta antes que Spring MVC.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> origins = Arrays.stream(allowedOriginsRaw.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        // Si hay alguna URL de Vercel configurada, también aceptar el patrón de previews
        boolean hasVercel = origins.stream().anyMatch(o -> o.contains("vercel.app"));
        if (hasVercel) {
            config.setAllowedOriginPatterns(List.of("*"));
        } else {
            config.setAllowedOrigins(origins);
        }

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // ── Acceso público: autenticación ────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/register").permitAll()

                        // ── Acceso público: Swagger ───────────────────────────────────
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/webjars/**")
                        .permitAll()

                        // ── Acceso público: ver restaurantes ──────────────────────────
                        .requestMatchers(HttpMethod.GET, "/api/restaurants", "/api/restaurants/{id}").permitAll()

                        // ── Acceso público: enviar solicitud de restaurante ───────────
                        .requestMatchers(HttpMethod.POST, "/api/restaurants/requests").permitAll()

                        // ── Acceso público: comentarios ───────────────────────────────
                        .requestMatchers(HttpMethod.POST, "/api/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()

                        // ── Acceso público: ver y reservar mesas ──────────────────────
                        .requestMatchers(HttpMethod.GET, "/api/tables/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/tables/reserve").permitAll()

                        // ── Cualquier usuario autenticado: ver su propio perfil ───────
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/me").authenticated()

                        // ── Tarea 3.5: reservas del usuario autenticado ───────────────
                        .requestMatchers(HttpMethod.GET, "/api/reservations/me").authenticated()

                        // ── Usuario autenticado: cancelar su propia reserva ───────────
                        .requestMatchers(HttpMethod.PATCH, "/api/reservations/*/status").authenticated()

                        // ── ADMINISTRADOR: gestión de solicitudes ─────────────────────
                        .requestMatchers("/api/restaurants/requests/**").hasRole("ADMINISTRADOR")

                        // ── ADMINISTRADOR: eliminar restaurantes ──────────────────────
                        .requestMatchers(HttpMethod.DELETE, "/api/restaurants/**").hasRole("ADMINISTRADOR")

                        // ── ADMINISTRADOR: gestión de usuarios (resto del CRUD) ───────
                        .requestMatchers("/api/users/**").hasRole("ADMINISTRADOR")

                        // ── PERSONAL y ADMINISTRADOR: editar restaurante ──────────────
                        .requestMatchers(HttpMethod.PUT, "/api/restaurants/{id}")
                        .hasAnyRole("ADMINISTRADOR", "PERSONAL")

                        // ── PERSONAL y ADMINISTRADOR: gestión de mesas ────────────────
                        .requestMatchers(HttpMethod.POST, "/api/tables").hasAnyRole("ADMINISTRADOR", "PERSONAL")
                        .requestMatchers(HttpMethod.DELETE, "/api/tables/**").hasAnyRole("ADMINISTRADOR", "PERSONAL")

                        // ── PERSONAL y ADMINISTRADOR: ver y gestionar reservas ────────
                        .requestMatchers(HttpMethod.GET, "/api/reservations/**").hasAnyRole("ADMINISTRADOR", "PERSONAL")

                        // ── USUARIO: ver su propia cuenta ────────────────────────────
                        .requestMatchers("/api/account/**").hasAnyRole("USUARIO", "ADMINISTRADOR")

                        // ── Todo lo demás requiere estar autenticado ──────────────────
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtService, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}