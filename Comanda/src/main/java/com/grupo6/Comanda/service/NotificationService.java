package com.grupo6.Comanda.service;

import com.grupo6.Comanda.model.entities.CommentEntity;
import com.grupo6.Comanda.model.entities.ReservationEntity;

/**
 * Servicio de notificaciones.
 * Implementación base: log en consola.
 * Para producción: inyectar JavaMailSender (spring-boot-starter-mail).
 */
public interface NotificationService {

    /** Notifica al cliente cuando el restaurante cancela su reserva. */
    void notificarCancelacionReserva(ReservationEntity reserva, String motivo);

    /** Notifica al cliente cuando el restaurante responde su comentario. */
    void notificarRespuestaComentario(CommentEntity comentario);
}