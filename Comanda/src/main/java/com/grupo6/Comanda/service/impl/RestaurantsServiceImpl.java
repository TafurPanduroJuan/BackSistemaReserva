package com.grupo6.Comanda.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;
import com.grupo6.Comanda.model.entities.ReservationEntity;
import com.grupo6.Comanda.repository.RestaurantRepository;
import com.grupo6.Comanda.repository.RestaurantRequestRepository;
import com.grupo6.Comanda.repository.ReservationRepository;
import com.grupo6.Comanda.service.NotificationService;
import com.grupo6.Comanda.service.RestaurantsService;

import java.time.format.DateTimeFormatter;

@Service
public class RestaurantsServiceImpl implements RestaurantsService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantRequestRepository requestRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public RestaurantsServiceImpl(RestaurantRepository restaurantRepository,
            RestaurantRequestRepository requestRepository,
            ReservationRepository reservationRepository,
            NotificationService notificationService) {
        this.restaurantRepository = restaurantRepository;
        this.requestRepository = requestRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
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
            if (updated.getTelefono() != null) {
                existing.setTelefono(updated.getTelefono());
            }
            if (updated.getEmail() != null && !updated.getEmail().isBlank()) {
                existing.setEmail(updated.getEmail().trim());
            }
            if (updated.getImagen() != null) {
                existing.setImagen(updated.getImagen());
            }
            // Horarios por día (null = cerrado ese día)
            existing.setHorarioLunes(updated.getHorarioLunes());
            existing.setHorarioMartes(updated.getHorarioMartes());
            existing.setHorarioMiercoles(updated.getHorarioMiercoles());
            existing.setHorarioJueves(updated.getHorarioJueves());
            existing.setHorarioViernes(updated.getHorarioViernes());
            existing.setHorarioSabado(updated.getHorarioSabado());
            existing.setHorarioDomingo(updated.getHorarioDomingo());

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
                // Copiar horarios por día desde la solicitud
                r.setHorarioLunes(req.getHorarioLunes());
                r.setHorarioMartes(req.getHorarioMartes());
                r.setHorarioMiercoles(req.getHorarioMiercoles());
                r.setHorarioJueves(req.getHorarioJueves());
                r.setHorarioViernes(req.getHorarioViernes());
                r.setHorarioSabado(req.getHorarioSabado());
                r.setHorarioDomingo(req.getHorarioDomingo());
                restaurantRepository.save(r);
            }

            // Notificar al solicitante que su restaurante fue aceptado
            notificationService.notificarAceptacionSolicitudRestaurante(req);

            return ResponseEntity.ok(Map.<String, Object>of("message", "Request accepted", "requestId", id));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Map<String, Object>> rejectRequest(Long id, String motivo) {
        return requestRepository.findById(id).map(req -> {
            req.setEstado("rechazado");
            requestRepository.save(req);

            // Notificar al solicitante con el motivo del rechazo
            notificationService.notificarRechazoSolicitudRestaurante(req, motivo);

            return ResponseEntity.ok(Map.<String, Object>of("message", "Request rejected", "requestId", id));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<RestaurantEntity> toggleCierre(Long id, Map<String, Object> body) {
        return restaurantRepository.findById(id).map(restaurant -> {
            Boolean cerrado = (Boolean) body.get("cerrado");
            String motivo = (String) body.get("motivoCierre");
            String fecha = (String) body.get("fecha"); // YYYY-MM-DD, fecha a cerrar

            restaurant.setCerradoHoy(cerrado != null ? cerrado : false);
            restaurant.setMotivoCierre(cerrado != null && cerrado ? motivo : null);

            RestaurantEntity guardado = restaurantRepository.save(restaurant);

            // Si se está programando un cierre, cancelar las reservas de esa fecha con el
            // motivo
            if (cerrado != null && cerrado && fecha != null && !fecha.isBlank()) {
                List<ReservationEntity> reservasRestaurante = reservationRepository.findByRestaurant_Id(id);

                for (ReservationEntity reserva : reservasRestaurante) {
                    boolean esLaFecha = fecha.equals(reserva.getFecha());
                    boolean estaActiva = "pendiente".equals(reserva.getEstado())
                            || "confirmada".equals(reserva.getEstado());

                    if (esLaFecha && estaActiva) {
                        reserva.setEstado("cancelada");
                        reserva.setMotivoCancelacion(
                                "El restaurante cerró por: " + (motivo != null ? motivo : "inconvenientes operativos"));
                        reservationRepository.save(reserva);
                        // Notificar al cliente de la cancelación por cierre
                        notificationService.notificarCancelacionReserva(reserva, reserva.getMotivoCancelacion());
                    }
                }
            }

            return ResponseEntity.ok(guardado);
        }).orElse(ResponseEntity.notFound().build());
    }
}