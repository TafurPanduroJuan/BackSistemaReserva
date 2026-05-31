package com.grupo6.Comanda.service.impl;

import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;
import com.grupo6.Comanda.repository.RestaurantRepository;
import com.grupo6.Comanda.repository.RestaurantRequestRepository;
import com.grupo6.Comanda.service.RestaurantsService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class RestaurantsServiceImpl implements RestaurantsService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantRequestRepository requestRepository;

    public RestaurantsServiceImpl(RestaurantRepository restaurantRepository,
                                  RestaurantRequestRepository requestRepository) {
        this.restaurantRepository = restaurantRepository;
        this.requestRepository    = requestRepository;
    }

    @Override
    public List<RestaurantEntity> listAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public ResponseEntity<RestaurantEntity> getOne(Long id) {
        return restaurantRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public RestaurantEntity create(RestaurantEntity restaurant) {
        restaurant.setId(null);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public ResponseEntity<RestaurantEntity> update(Long id, RestaurantEntity updated) {
        return restaurantRepository.findById(id).map(existing -> {
            updated.setId(id);
            return ResponseEntity.ok(restaurantRepository.save(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        if (!restaurantRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        restaurantRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<RestaurantRequestEntity> listRequests(String estado) {
        return (estado == null || estado.isBlank())
                ? requestRepository.findAll()
                : requestRepository.findByEstado(estado);
    }

    @Override
    public RestaurantRequestEntity submitRequest(RestaurantRequestEntity req) {
        req.setId(null);
        req.setEstado("pendiente");
        if (req.getFecha() == null || req.getFecha().isBlank()) {
            req.setFecha(LocalDate.now().toString());
        }
        return requestRepository.save(req);
    }

    @Override
    public ResponseEntity<Map<String, Object>> acceptRequest(Long id) {
        return requestRepository.findById(id).map(req -> {
            req.setEstado("aceptado");
            requestRepository.save(req);

            // Auto-create restaurant entry if it doesn't exist yet
            if (restaurantRepository.findByNombre(req.getNombre()).isEmpty()) {
                RestaurantEntity r = new RestaurantEntity();
                r.setNombre(req.getNombre());
                r.setTipo(req.getTipo());
                r.setDistrito(req.getCiudad());
                r.setDireccion("");
                r.setMensajePersonalizado(req.getDescripcion());
                r.setMesas(0);
                r.setTelefono(req.getTelefono());
                r.setEmail(req.getEmail());
                r.setImagen(null);
                r.setHorarioApertura("");
                r.setHorarioCierre("");
                restaurantRepository.save(r);
            }

            return ResponseEntity.ok(Map.<String, Object>of("message", "Request accepted", "requestId", id));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Map<String, Object>> rejectRequest(Long id) {
        return requestRepository.findById(id).map(req -> {
            req.setEstado("rechazado");
            requestRepository.save(req);
            return ResponseEntity.ok(Map.<String, Object>of("message", "Request rejected", "requestId", id));
        }).orElse(ResponseEntity.notFound().build());
    }
}