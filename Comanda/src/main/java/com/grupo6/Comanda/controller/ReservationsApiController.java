package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.controller.dto.UpdateStatusRequest;
import com.grupo6.Comanda.model.entities.ReservationEntity;
import com.grupo6.Comanda.repository.ReservationRepository;
import com.grupo6.Comanda.repository.TableRepository;
import com.grupo6.Comanda.service.ReservationsService;
import com.grupo6.Comanda.service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoints used by the React frontend:
 *
 *   GET    /api/reservations?restaurantId=&fecha=&estado=   → filtered list (admin/personal)
 *   GET    /api/reservations/me                             → reservas del usuario autenticado
 *   PATCH  /api/reservations/{id}/status                    → update status (+ motivoCancelacion)
 */
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin
public class ReservationsApiController {

    private final ReservationsService service;
    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final NotificationService notificationService;

    public ReservationsApiController(ReservationsService service,
                                     ReservationRepository reservationRepository,
                                     TableRepository tableRepository,
                                     NotificationService notificationService) {
        this.service = service;
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.notificationService = notificationService;
    }

    /**
     * Listado filtrado para intranet (admin / personal).
     */
    @GetMapping
    public ResponseEntity<List<ReservationEntity>> getReservations(
            @RequestParam("restaurantId") Long restaurantId,
            @RequestParam(value = "fecha", required = false) String fecha,
            @RequestParam(value = "estado", required = false) String estado
    ) {
        if (fecha == null || fecha.isBlank()) {
            return ResponseEntity.ok(reservationRepository.findByRestaurant_Id(restaurantId));
        }
        return ResponseEntity.ok(service.getReservations(restaurantId, fecha, estado));
    }

    /**
     * Reservas del usuario autenticado.
     * Filtra por el email extraído del JWT.
     */
    @GetMapping("/me")
    public ResponseEntity<List<ReservationEntity>> getMyReservations(
            Authentication authentication) {
        String email = authentication.getName();
        List<ReservationEntity> reservas = reservationRepository.findByEmail(email);
        return ResponseEntity.ok(reservas);
    }

    /**
     * Actualizar estado de una reserva.
     * Body: { "estado": "cancelada", "motivoCancelacion": "..." }
     * Valores válidos: pendiente | confirmada | cancelada | cancelada_cliente
     *
     * Al cancelar (por admin/personal) o rechazar, se envía notificación por email al cliente.
     */
    @Transactional
    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest body) {

        if (body.getEstado() == null || body.getEstado().isBlank()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Missing 'estado' field");
            return ResponseEntity.badRequest().body(error);
        }

        String estadoFinal = body.getEstado();

        return reservationRepository.findById(id)
                .map(res -> {
                    String nuevoEstado = estadoFinal.toLowerCase();
                    String estadoAnterior = res.getEstado();
                    res.setEstado(nuevoEstado);

                    // Guardar motivo de cancelación si viene en el body
                    if (body.getMotivoCancelacion() != null && !body.getMotivoCancelacion().isBlank()) {
                        res.setMotivoCancelacion(body.getMotivoCancelacion());
                    }

                    reservationRepository.save(res);

                    // Si la reserva se cancela (por admin o por cliente), liberar la mesa
                    boolean esCancelacion = nuevoEstado.startsWith("cancelada");
                    if (esCancelacion && res.getMesaNumero() != null && res.getRestaurant() != null) {
                        tableRepository
                            .findByRestaurant_IdAndNumero(res.getRestaurant().getId(), res.getMesaNumero())
                            .ifPresent(mesa -> {
                                mesa.setEstado("disponible");
                                tableRepository.save(mesa);
                            });
                    }

                    // Enviar notificación por email al cliente cuando el restaurante cancela
                    // (cancelada = cancelada por el restaurante, no por el cliente)
                    if ("cancelada".equals(nuevoEstado) && !"cancelada".equals(estadoAnterior)) {
                        notificationService.notificarCancelacionReserva(res, body.getMotivoCancelacion());
                    }

                    Map<String, Object> resp = new HashMap<>();
                    resp.put("message", "Status updated");
                    resp.put("id", id);
                    resp.put("estado", nuevoEstado);
                    return ResponseEntity.<Map<String, Object>>ok(resp);
                })
                .orElseGet(() -> ResponseEntity.<Map<String, Object>>notFound().build());
    }
}