package com.grupo6.Comanda.service;

import com.grupo6.Comanda.model.entities.CommentEntity;
import com.grupo6.Comanda.model.entities.ReservationEntity;
import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;

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

    /** Notifica al solicitante cuando su solicitud de restaurante es rechazada. */
    void notificarRechazoSolicitudRestaurante(RestaurantRequestEntity solicitud, String motivo);

    /** Notifica al solicitante cuando su solicitud de restaurante es aceptada. */
    void notificarAceptacionSolicitudRestaurante(RestaurantRequestEntity solicitud);
}