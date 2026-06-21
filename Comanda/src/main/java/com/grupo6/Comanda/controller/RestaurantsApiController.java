package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;
import com.grupo6.Comanda.service.RestaurantsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;  // ← IMPORT NUEVO
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin
public class RestaurantsApiController {

    private final RestaurantsService restaurantsService;

    public RestaurantsApiController(RestaurantsService restaurantsService) {
        this.restaurantsService = restaurantsService;
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

    @PreAuthorize("hasRole('ADMINISTRADOR')")          // ← NUEVO
    @PostMapping
    public ResponseEntity<RestaurantEntity> create(@RequestBody RestaurantEntity restaurant) {
        return ResponseEntity.ok(restaurantsService.create(restaurant));
    }

    @PreAuthorize(                                     // ← NUEVO
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

    @PreAuthorize("hasRole('ADMINISTRADOR')")          // ← NUEVO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return restaurantsService.delete(id);
    }

    // ── Restaurant requests ──────────────────────────────────────────────────

    @PreAuthorize("hasRole('ADMINISTRADOR')")          // ← NUEVO
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

    @PreAuthorize("hasRole('ADMINISTRADOR')")          // ← NUEVO
    @PutMapping("/requests/{id}/accept")
    public ResponseEntity<Map<String, Object>> acceptRequest(@PathVariable Long id) {
        return restaurantsService.acceptRequest(id);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")          // ← NUEVO
    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectRequest(@PathVariable Long id) {
        return restaurantsService.rejectRequest(id);
    }
}