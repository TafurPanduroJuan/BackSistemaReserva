package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.controller.dto.UpdateStatusRequest;
import com.grupo6.Comanda.model.entities.ReservationEntity;
import com.grupo6.Comanda.repository.ReservationRepository;
import com.grupo6.Comanda.service.ReservationsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoints used by the React frontend:
 *
 *   GET    /api/reservations?restaurantId=&fecha=&estado=   → filtered list (admin/personal)
 *   GET    /api/reservations/me                             → reservas del usuario autenticado
 *   PATCH  /api/reservations/{id}/status                    → update status
 */
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin
public class ReservationsApiController {

    private final ReservationsService service;
    private final ReservationRepository reservationRepository;

    public ReservationsApiController(ReservationsService service,
                                     ReservationRepository reservationRepository) {
        this.service = service;
        this.reservationRepository = reservationRepository;
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
        String email = authentication.getName(); // email del JWT
        List<ReservationEntity> reservas = reservationRepository.findByEmail(email);
        return ResponseEntity.ok(reservas);
    }

    /**
     * Actualizar estado de una reserva.
     * Body: { "estado": "confirmada" }
     * Valores válidos: pendiente | confirmada | cancelada | cancelada_cliente
     */
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
                    res.setEstado(estadoFinal.toLowerCase());
                    reservationRepository.save(res);
                    Map<String, Object> resp = new HashMap<>();
                    resp.put("message", "Status updated");
                    resp.put("id", id);
                    resp.put("estado", estadoFinal);
                    return ResponseEntity.<Map<String, Object>>ok(resp);
                })
                .orElseGet(() -> ResponseEntity.<Map<String, Object>>notFound().build());
    }
}