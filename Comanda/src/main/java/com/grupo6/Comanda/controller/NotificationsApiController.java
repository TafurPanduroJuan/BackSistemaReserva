package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.model.entities.NotificationEntity;
import com.grupo6.Comanda.service.InAppNotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Endpoints de notificaciones en-app para el usuario autenticado.
 *
 *   GET    /api/notifications/me            → lista de notificaciones (máx 50)
 *   GET    /api/notifications/me/count      → { "unread": N }
 *   POST   /api/notifications/me/read-all  → marca todas como leídas
 *   POST   /api/notifications/{id}/read    → marca una como leída
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationsApiController {

    private final InAppNotificationService service;

    public NotificationsApiController(InAppNotificationService service) {
        this.service = service;
    }

    /** Lista completa de notificaciones del usuario autenticado */
    @GetMapping("/me")
    public ResponseEntity<List<NotificationEntity>> listar(Authentication auth) {
        return ResponseEntity.ok(service.listar(auth.getName()));
    }

    /** Conteo de no leídas */
    @GetMapping("/me/count")
    public ResponseEntity<Map<String, Long>> count(Authentication auth) {
        long unread = service.contarNoLeidas(auth.getName());
        return ResponseEntity.ok(Map.of("unread", unread));
    }

    /** Marca todas las notificaciones del usuario como leídas */
    @PostMapping("/me/read-all")
    public ResponseEntity<Map<String, String>> readAll(Authentication auth) {
        service.marcarTodasLeidas(auth.getName());
        return ResponseEntity.ok(Map.of("message", "Todas marcadas como leídas"));
    }

    /** Marca una notificación específica como leída */
    @PostMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> readOne(@PathVariable Long id) {
        service.marcarLeida(id);
        return ResponseEntity.ok(Map.of("message", "Notificación marcada como leída"));
    }
}