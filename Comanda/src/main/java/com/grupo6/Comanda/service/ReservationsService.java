package com.grupo6.Comanda.service;

import com.grupo6.Comanda.model.entities.ReservationEntity;

import java.util.List;

public interface ReservationsService {

    List<ReservationEntity> getReservations(Long restaurantId, String fecha, String estado);

}

