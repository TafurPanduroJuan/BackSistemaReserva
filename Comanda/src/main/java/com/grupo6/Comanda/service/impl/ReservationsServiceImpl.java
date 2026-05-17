package com.grupo6.Comanda.service.impl;

import com.grupo6.Comanda.model.entities.ReservationEntity;
import com.grupo6.Comanda.repository.ReservationRepository;
import com.grupo6.Comanda.service.ReservationsService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationsServiceImpl implements ReservationsService {

    private final ReservationRepository reservationRepository;

    public ReservationsServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<ReservationEntity> getReservations(Long restaurantId, String fecha, String estado) {
        if (restaurantId == null || fecha == null) {
            return List.of();
        }

        if (estado == null || estado.isBlank()) {
            return reservationRepository.findByRestaurant_IdAndFecha(restaurantId, fecha);
        }

        // Normalize: accept both Spanish frontend values and raw DB values
        String normalized = switch (estado.toLowerCase()) {
            case "pendiente"         -> "pendiente";
            case "confirmada"        -> "confirmada";
            case "cancelada"         -> "cancelada";
            case "cancelada_cliente" -> "cancelada_cliente";
            // Map legacy English names just in case
            case "pending"           -> "pendiente";
            case "confirmed"         -> "confirmada";
            case "canceled"          -> "cancelada";
            default                  -> estado.toLowerCase();
        };

        return reservationRepository.findByRestaurant_IdAndFechaAndEstado(restaurantId, fecha, normalized);
    }
}
