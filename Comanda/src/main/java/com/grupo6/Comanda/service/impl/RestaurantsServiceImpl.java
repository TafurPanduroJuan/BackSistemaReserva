package com.grupo6.Comanda.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;
import com.grupo6.Comanda.repository.RestaurantRepository;
import com.grupo6.Comanda.repository.RestaurantRequestRepository;
import com.grupo6.Comanda.service.RestaurantsService;

import com.grupo6.Comanda.repository.ReservationRepository;
import com.grupo6.Comanda.model.entities.ReservationEntity;
import java.time.format.DateTimeFormatter;

@Service
public class RestaurantsServiceImpl implements RestaurantsService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantRequestRepository requestRepository;
    private final ReservationRepository reservationRepository;

    public RestaurantsServiceImpl(RestaurantRepository restaurantRepository,
            RestaurantRequestRepository requestRepository,
            ReservationRepository reservationRepository) {
        this.restaurantRepository = restaurantRepository;
        this.requestRepository = requestRepository;
        this.reservationRepository = reservationRepository;
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

            if (updated.getNombre() != null && !updated.getNombre().isBlank()) {
                existing.setNombre(updated.getNombre().trim());
            }
            if (updated.getTipo() != null && !updated.getTipo().isBlank()) {
                existing.setTipo(updated.getTipo().trim());
            }
            if (updated.getDistrito() != null && !updated.getDistrito().isBlank()) {
                existing.setDistrito(updated.getDistrito().trim());
            }
            if (updated.getDireccion() != null && !updated.getDireccion().isBlank()) {
                existing.setDireccion(updated.getDireccion().trim());
            }
            if (updated.getMensajePersonalizado() != null && !updated.getMensajePersonalizado().isBlank()) {
                existing.setMensajePersonalizado(updated.getMensajePersonalizado().trim());
            }
            if (updated.getMesas() != null) {
                existing.setMesas(updated.getMesas());
            }
            // telefono es Long — solo actualizar si viene informado
            if (updated.getTelefono() != null) {
                existing.setTelefono(updated.getTelefono());
            }
            if (updated.getEmail() != null && !updated.getEmail().isBlank()) {
                existing.setEmail(updated.getEmail().trim());
            }
            if (updated.getImagen() != null) {
                existing.setImagen(updated.getImagen());
            }
            if (updated.getHorarioApertura() != null && !updated.getHorarioApertura().isBlank()) {
                existing.setHorarioApertura(updated.getHorarioApertura().trim());
            }
            if (updated.getHorarioCierre() != null && !updated.getHorarioCierre().isBlank()) {
                existing.setHorarioCierre(updated.getHorarioCierre().trim());
            }

            return ResponseEntity.ok(restaurantRepository.save(existing));
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

            if (restaurantRepository.findByNombre(req.getNombre()).isEmpty()) {
                RestaurantEntity r = new RestaurantEntity();
                r.setNombre(req.getNombre());
                r.setTipo(req.getTipo());
                r.setDistrito(req.getDistrito());
                r.setDireccion(req.getDireccion() != null ? req.getDireccion() : "");
                r.setMensajePersonalizado(
                    req.getMensajePersonalizado() != null
                        ? req.getMensajePersonalizado()
                        : req.getDescripcion()
                );
                r.setMesas(0);
                r.setTelefono(req.getTelefono());
                r.setEmail(req.getEmail());
                r.setImagen(req.getImagen());
                r.setHorarioApertura(req.getHorarioApertura() != null ? req.getHorarioApertura() : "");
                r.setHorarioCierre(req.getHorarioCierre() != null ? req.getHorarioCierre() : "");
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

    @Override
    public ResponseEntity<RestaurantEntity> toggleCierre(Long id, Map<String, Object> body) {
        return restaurantRepository.findById(id).map(restaurant -> {
            Boolean cerrado = (Boolean) body.get("cerradoHoy");
            String motivo = (String) body.get("motivoCierre");

            restaurant.setCerradoHoy(cerrado != null ? cerrado : false);
            restaurant.setMotivoCierre(cerrado != null && cerrado ? motivo : null);

            return ResponseEntity.ok(restaurantRepository.save(restaurant));
        }).orElse(ResponseEntity.notFound().build());
    }
}