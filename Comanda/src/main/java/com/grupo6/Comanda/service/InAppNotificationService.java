package com.grupo6.Comanda.service;

import com.grupo6.Comanda.model.entities.NotificationEntity;

import java.util.List;

/**
 * Servicio para notificaciones en-app (panel en el navbar del usuario).
 */
public interface InAppNotificationService {

    /** Crea y persiste una notificación para el usuario. */
    NotificationEntity crear(String userEmail, String tipo, String mensaje,
                             Long reservaId, Long comentarioId);

    /** Todas las notificaciones del usuario (máx. 50, más recientes primero). */
    List<NotificationEntity> listar(String userEmail);

    /** Cantidad de notificaciones no leídas. */
    long contarNoLeidas(String userEmail);

    /** Marca todas las notificaciones del usuario como leídas. */
    void marcarTodasLeidas(String userEmail);

    /** Marca una notificación individual como leída. */
    void marcarLeida(Long id);
}