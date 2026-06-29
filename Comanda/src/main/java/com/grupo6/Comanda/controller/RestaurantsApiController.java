package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.controller.dto.RejectRequestBody;
import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;
import com.grupo6.Comanda.model.entities.CommentEntity;
import com.grupo6.Comanda.repository.CommentRepository;
import com.grupo6.Comanda.repository.TableRepository;
import com.grupo6.Comanda.service.RestaurantsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.OptionalDouble;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin
public class RestaurantsApiController {

    private final RestaurantsService restaurantsService;
    private final TableRepository tableRepository;
    private final CommentRepository commentRepository;

    public RestaurantsApiController(RestaurantsService restaurantsService,
                                    TableRepository tableRepository,
                                    CommentRepository commentRepository) {
        this.restaurantsService = restaurantsService;
        this.tableRepository = tableRepository;
        this.commentRepository = commentRepository;
    }

    // ── Restaurants ──────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<RestaurantEntity>> listAll() {
        return ResponseEntity.ok(restaurantsService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantEntity> getOne(@PathVariable Long id) {
        return restaurantsService.getOne(id);
    }

    /**
     * Stats públicas de un restaurante: total de mesas, rating promedio y total de reseñas.
     * GET /api/restaurants/{id}/stats
     */
    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getStats(@PathVariable Long id) {
        // Total de mesas
        long totalMesas = tableRepository.findByRestaurant_Id(id).size();

        // Comentarios con calificación
        List<CommentEntity> comentarios = commentRepository.findByRestaurant_Id(id);
        List<Integer> calificaciones = comentarios.stream()
            .map(CommentEntity::getCalificacion)
            .filter(c -> c != null && c >= 1 && c <= 5)
            .toList();

        long totalResenas = calificaciones.size();
        OptionalDouble promedio = calificaciones.stream()
            .mapToInt(Integer::intValue)
            .average();
        double rating = promedio.isPresent()
            ? Math.round(promedio.getAsDouble() * 10.0) / 10.0
            : 0.0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMesas", totalMesas);
        stats.put("totalResenas", totalResenas);
        stats.put("rating", rating);
        return ResponseEntity.ok(stats);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<RestaurantEntity> create(@RequestBody RestaurantEntity restaurant) {
        return ResponseEntity.ok(restaurantsService.create(restaurant));
    }

    @PreAuthorize(
        "hasRole('ADMINISTRADOR') or " +
        "(hasRole('PERSONAL') and @restaurantSecurityService.esPropietario(authentication, #id))"
    )
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantEntity> update(@PathVariable Long id,
                                                    @RequestBody RestaurantEntity updated) {
        return restaurantsService.update(id, updated);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR') or " +
            "(hasRole('PERSONAL') and @restaurantSecurityService.esPropietario(authentication, #id))")
    @PutMapping("/{id}/cierre")
    public ResponseEntity<RestaurantEntity> toggleCierre(@PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        return restaurantsService.toggleCierre(id, body);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return restaurantsService.delete(id);
    }

    // ── Restaurant requests ──────────────────────────────────────────────────

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/requests")
    public ResponseEntity<List<RestaurantRequestEntity>> listRequests(
            @RequestParam(value = "estado", required = false) String estado) {
        return ResponseEntity.ok(restaurantsService.listRequests(estado));
    }

    /** Public endpoint: any visitor can submit a restaurant registration request. */
    @PostMapping("/requests")
    public ResponseEntity<RestaurantRequestEntity> submitRequest(
            @RequestBody RestaurantRequestEntity req) {
        return ResponseEntity.ok(restaurantsService.submitRequest(req));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/requests/{id}/accept")
    public ResponseEntity<Map<String, Object>> acceptRequest(@PathVariable Long id) {
        return restaurantsService.acceptRequest(id);
    }

    /**
     * Rechaza una solicitud de restaurante.
     * Body (opcional): { "motivo": "..." }
     * Si se proporciona motivo, se envía por email al solicitante.
     */
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectRequest(
            @PathVariable Long id,
            @RequestBody(required = false) RejectRequestBody body) {
        String motivo = (body != null) ? body.getMotivo() : null;
        return restaurantsService.rejectRequest(id, motivo);
    }
}