package main.java.com.grupo6.Comanda.controller;

import com.grupo6.Comanda.model.entities.ReservationEntity;
import com.grupo6.Comanda.repository.ReservationRepository;
import com.grupo6.Comanda.service.ReservationsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoints used by the React frontend (Reservas.jsx intranet page):
 *
 *   GET    /api/reservations?restaurantId=&fecha=&estado=   → filtered list
 *   GET    /api/reservations?restaurantId=                  → all for a restaurant
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
     * Update reservation status.
     * Body: { "estado": "confirmada" }
     * Valid values: pendiente | confirmada | cancelada | cancelada_cliente
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Missing 'estado' field");
            return ResponseEntity.badRequest().body(error);
        }

        String estadoFinal = nuevoEstado;

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