package com.grupo6.Comanda.service.impl;

import com.grupo6.Comanda.model.entities.CommentEntity;
import com.grupo6.Comanda.model.entities.ReservationEntity;
import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;
import com.grupo6.Comanda.service.NotificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servicio de notificaciones por email.
 *
 * Para activar el envío real de emails, configura en application.properties:
 *   spring.mail.host=smtp.gmail.com
 *   spring.mail.port=587
 *   spring.mail.username=${MAIL_USERNAME}
 *   spring.mail.password=${MAIL_PASSWORD}
 *   spring.mail.properties.mail.smtp.auth=true
 *   spring.mail.properties.mail.smtp.starttls.enable=true
 *   comanda.mail.from=noreply@comanda.pe
 *
 * Sin configuración de mail, el servicio solo loguea en consola.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${comanda.mail.from:noreply@comanda.pe}")
    private String fromAddress;

    @Value("${comanda.mail.enabled:false}")
    private boolean mailEnabled;

    // ── Reservas ──────────────────────────────────────────────────────────────

    @Override
    public void notificarCancelacionReserva(ReservationEntity reserva, String motivo) {
        String nombreRestaurante = reserva.getRestaurant() != null
                ? reserva.getRestaurant().getNombre()
                : "el restaurante";

        String asunto = "Tu reserva en " + nombreRestaurante + " ha sido cancelada";

        String cuerpo = "Hola " + reserva.getCliente() + ",\n\n"
                + "Lamentamos informarte que tu reserva en " + nombreRestaurante
                + " para el día " + reserva.getFecha() + " a las " + reserva.getHora()
                + " ha sido cancelada por el restaurante.\n\n"
                + (motivo != null && !motivo.isBlank()
                    ? "Motivo de cancelación: " + motivo + "\n\n"
                    : "")
                + "Si tienes dudas, no dudes en contactarnos.\n\n"
                + "El equipo de Comanda";

        enviarEmail(reserva.getEmail(), asunto, cuerpo);
    }

    // ── Comentarios ───────────────────────────────────────────────────────────

    @Override
    public void notificarRespuestaComentario(CommentEntity comentario) {
        String nombreRestaurante = comentario.getRestaurant() != null
                ? comentario.getRestaurant().getNombre()
                : "el restaurante";

        String asunto = nombreRestaurante + " respondió a tu comentario";

        String cuerpo = "Hola " + comentario.getUsuario() + ",\n\n"
                + nombreRestaurante + " ha respondido a tu " + comentario.getTipo()
                + " con asunto \"" + comentario.getAsunto() + "\":\n\n"
                + "\"" + comentario.getRespuestaRestaurante() + "\"\n\n"
                + "Puedes ver todos los detalles en tu perfil de Comanda.\n\n"
                + "El equipo de Comanda";

        enviarEmail(comentario.getEmail(), asunto, cuerpo);
    }

    // ── Solicitudes de restaurante ────────────────────────────────────────────

    @Override
    public void notificarRechazoSolicitudRestaurante(RestaurantRequestEntity solicitud, String motivo) {
        String asunto = "Tu solicitud de registro \"" + solicitud.getNombre() + "\" no fue aprobada";

        String cuerpo = "Hola " + solicitud.getPropietario() + ",\n\n"
                + "Gracias por tu interés en formar parte de Comanda.\n\n"
                + "Lamentamos informarte que tu solicitud de registro para el restaurante \""
                + solicitud.getNombre() + "\" no ha sido aprobada en esta ocasión.\n\n"
                + (motivo != null && !motivo.isBlank()
                    ? "Motivo: " + motivo + "\n\n"
                    : "")
                + "Si crees que existe algún error o deseas más información, "
                + "puedes ponerte en contacto con nuestro equipo de soporte.\n\n"
                + "El equipo de Comanda";

        enviarEmail(solicitud.getEmail(), asunto, cuerpo);
    }

    @Override
    public void notificarAceptacionSolicitudRestaurante(RestaurantRequestEntity solicitud) {
        String asunto = "¡Tu restaurante \"" + solicitud.getNombre() + "\" ha sido aprobado en Comanda!";

        String cuerpo = "Hola " + solicitud.getPropietario() + ",\n\n"
                + "¡Excelentes noticias! Tu solicitud de registro para el restaurante \""
                + solicitud.getNombre() + "\" ha sido aprobada.\n\n"
                + "Tu restaurante ya está disponible en nuestra plataforma. "
                + "En breve nos pondremos en contacto para configurar el acceso a tu panel de gestión.\n\n"
                + "¡Bienvenido a Comanda!\n\n"
                + "El equipo de Comanda";

        enviarEmail(solicitud.getEmail(), asunto, cuerpo);
    }

    // ── Envío interno ─────────────────────────────────────────────────────────

    private void enviarEmail(String destinatario, String asunto, String cuerpo) {
        log.info("[NOTIFICACIÓN] Para: {} | Asunto: {}", destinatario, asunto);
        log.debug("[NOTIFICACIÓN] Cuerpo:\n{}", cuerpo);

        if (!mailEnabled || mailSender == null) {
            log.info("[NOTIFICACIÓN] Email desactivado. Configura 'comanda.mail.enabled=true' y spring.mail.* para activar.");
            return;
        }

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(fromAddress);
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);
            mailSender.send(mensaje);
            log.info("[NOTIFICACIÓN] Email enviado exitosamente a {}", destinatario);
        } catch (Exception e) {
            log.error("[NOTIFICACIÓN] Error enviando email a {}: {}", destinatario, e.getMessage());
        }
    }
}