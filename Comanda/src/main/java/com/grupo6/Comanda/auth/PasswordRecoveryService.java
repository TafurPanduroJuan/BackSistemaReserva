package com.grupo6.Comanda.auth;

import com.grupo6.Comanda.model.entities.UserEntity;
import com.grupo6.Comanda.repository.UserRepository;
import com.grupo6.Comanda.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class PasswordRecoveryService {

    private static final long TOKEN_VALIDITY_MS = 60 * 60 * 1000L;

    private final UserRepository userRepository;
    private final EmailService   emailService;
    private final PasswordHasher passwordHasher;

    @Value("${comanda.mail.from:noreply@comanda.pe}")
    private String mailFrom;

    @Value("${comanda.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${comanda.mail.enabled:false}")
    private boolean mailEnabled;

    public PasswordRecoveryService(UserRepository userRepository,
                                   EmailService emailService,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService   = emailService;
        this.passwordHasher = PasswordHasher.from(passwordEncoder);
    }

    public void requestReset(String email) {
        Optional<UserEntity> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) {
            opt = userRepository.findByGoogleEmail(email);
        }
        if (opt.isEmpty()) return;

        UserEntity user = opt.get();

        String sendTo = userRepository.findByEmail(email).isPresent()
                ? user.getEmail()
                : user.getGoogleEmail();

        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        user.setPasswordResetToken(token);
        user.setPasswordResetExpires(System.currentTimeMillis() + TOKEN_VALIDITY_MS);
        userRepository.save(user);

        String resetLink = frontendUrl + "/reset-password?token=" + token;

        if (mailEnabled) {
            sendResetEmail(sendTo, user.getName(), resetLink);
        } else {
            System.out.println("=== [COMANDA - RECUPERACIÓN DE CONTRASEÑA] ===");
            System.out.println("Para: " + sendTo);
            System.out.println("Link: " + resetLink);
            System.out.println("Token expira en 1 hora.");
            System.out.println("==============================================");
        }
    }

    public void confirmReset(String token, String newPassword) {
        UserEntity user = userRepository.findByPasswordResetToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Token inválido o ya fue usado"));

        if (user.getPasswordResetExpires() == null
                || System.currentTimeMillis() > user.getPasswordResetExpires()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El enlace de recuperación expiró");
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

    private void sendResetEmail(String to, String nombre, String link) {
        String subject = "Comanda — Recupera tu contraseña";
        String body =
            "Hola " + nombre + ",\n\n" +
            "Recibimos una solicitud para restablecer la contraseña de tu cuenta Comanda.\n\n" +
            "Haz clic en el siguiente enlace para elegir una nueva contraseña:\n\n" +
            link + "\n\n" +
            "Este enlace es válido por 1 hora.\n\n" +
            "Si no solicitaste este cambio, ignora este correo.\n\n" +
            "— Equipo Comanda";
        try {
            emailService.sendEmail(to, subject, body);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar email: " + e.getMessage());
        }
    }
}