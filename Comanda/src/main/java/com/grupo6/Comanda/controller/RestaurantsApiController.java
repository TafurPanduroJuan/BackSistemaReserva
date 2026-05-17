package main.java.com.grupo6.Comanda.controller;

import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;
import com.grupo6.Comanda.service.RestaurantsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for restaurants and restaurant-registration requests.
 *
 *   GET    /api/restaurants                       → list all restaurants
 *   GET    /api/restaurants/{id}                  → get one
 *   POST   /api/restaurants                       → create (admin)
 *   PUT    /api/restaurants/{id}                  → edit (admin)
 *   DELETE /api/restaurants/{id}                  → delete (admin)
 *
 *   GET    /api/restaurants/requests              → list requests
 *   POST   /api/restaurants/requests              → submit new request (public)
 *   PUT    /api/restaurants/requests/{id}/accept  → accept (admin)
 *   PUT    /api/restaurants/requests/{id}/reject  → reject (admin)
 */
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

    @PostMapping
    public ResponseEntity<RestaurantEntity> create(@RequestBody RestaurantEntity restaurant) {
        return ResponseEntity.ok(restaurantsService.create(restaurant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantEntity> update(@PathVariable Long id,
                                                    @RequestBody RestaurantEntity updated) {
        return restaurantsService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return restaurantsService.delete(id);
    }

    // ── Restaurant requests ──────────────────────────────────────────────────

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

    @PutMapping("/requests/{id}/accept")
    public ResponseEntity<Map<String, Object>> acceptRequest(@PathVariable Long id) {
        return restaurantsService.acceptRequest(id);
    }

    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<Map<String, Object>> rejectRequest(@PathVariable Long id) {
        return restaurantsService.rejectRequest(id);
    }
}