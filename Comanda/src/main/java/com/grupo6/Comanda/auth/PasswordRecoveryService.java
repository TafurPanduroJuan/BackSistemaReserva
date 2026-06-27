package com.grupo6.Comanda.auth;

import com.grupo6.Comanda.model.entities.UserEntity;
import com.grupo6.Comanda.repository.UserRepository;
import com.grupo6.Comanda.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

/**
 * Gestiona la recuperación de contraseña vía correo electrónico.
 *
 * Flujo:
 *   1. Usuario ingresa su email de Comanda O su google_email vinculado.
 *   2. El sistema busca la cuenta, genera un token de 1 hora y envía email.
 *   3. El usuario hace clic en el link, ingresa nueva contraseña + token.
 *   4. Backend valida token, actualiza hash, borra token.
 */
@Service
public class PasswordRecoveryService {

    private static final long TOKEN_VALIDITY_MS = 60 * 60 * 1000L; // 1 hora

    private final UserRepository    userRepository;
    private final JavaMailSender    mailSender;
    private final PasswordHasher    passwordHasher;

    @Value("${comanda.mail.from:noreply@comanda.pe}")
    private String mailFrom;

    @Value("${comanda.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${comanda.mail.enabled:false}")
    private boolean mailEnabled;

    public PasswordRecoveryService(UserRepository userRepository,
                                   JavaMailSender mailSender,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mailSender     = mailSender;
        this.passwordHasher = PasswordHasher.from(passwordEncoder);
    }

    // ── Paso 1: solicitar recuperación ────────────────────────────────────────
    public void requestReset(String email) {
        // Buscar por email principal O por google_email vinculado
        Optional<UserEntity> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            opt = userRepository.findByGoogleEmail(email);
        }

        // Si no existe, devolver igual (no revelar si el correo existe)
        if (opt.isEmpty()) return;

        UserEntity user = opt.get();

        // Determinar a qué correo enviar el link
        // Si el usuario ingresó su google_email, enviamos al google_email
        // Si ingresó el email principal, enviamos al email principal
        String sendTo = userRepository.findByEmail(email).isPresent()
                ? user.getEmail()
                : user.getGoogleEmail();

        // Generar token seguro
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        user.setPasswordResetToken(token);
        user.setPasswordResetExpires(System.currentTimeMillis() + TOKEN_VALIDITY_MS);
        userRepository.save(user);

        // Construir link
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        if (mailEnabled) {
            sendResetEmail(sendTo, user.getName(), resetLink);
        } else {
            // En desarrollo: imprimir en consola
            System.out.println("=== [COMANDA - RECUPERACIÓN DE CONTRASEÑA] ===");
            System.out.println("Para: " + sendTo);
            System.out.println("Link: " + resetLink);
            System.out.println("Token expira en 1 hora.");
            System.out.println("==============================================");
        }
    }

    // ── Paso 2: confirmar nueva contraseña ────────────────────────────────────
    public void confirmReset(String token, String newPassword) {
        UserEntity user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Token inválido o ya fue usado"));

        if (user.getPasswordResetExpires() == null
                || System.currentTimeMillis() > user.getPasswordResetExpires()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El enlace de recuperación expiró");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 6 caracteres");
        }

        user.setPasswordHash(passwordHasher.hash(newPassword));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpires(null);
        userRepository.save(user);
    }

    // ── Email ─────────────────────────────────────────────────────────────────
    private void sendResetEmail(String to, String nombre, String link) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(mailFrom);
        msg.setTo(to);
        msg.setSubject("Comanda — Recupera tu contraseña");
        msg.setText(
            "Hola " + nombre + ",\n\n" +
            "Recibimos una solicitud para restablecer la contraseña de tu cuenta Comanda.\n\n" +
            "Haz clic en el siguiente enlace para elegir una nueva contraseña:\n\n" +
            link + "\n\n" +
            "Este enlace es válido por 1 hora.\n\n" +
            "Si no solicitaste este cambio, ignora este correo: tu contraseña actual no cambiará.\n\n" +
            "— Equipo Comanda"
        );
        mailSender.send(msg);
    }
}
