package com.grupo6.Comanda.repository;

import com.grupo6.Comanda.model.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByRestaurant_IdAndFecha(Long restaurantId, String fecha);
    List<ReservationEntity> findByRestaurant_IdAndFechaAndEstado(Long restaurantId, String fecha, String estado);
    List<ReservationEntity> findByRestaurant_IdAndEstado(Long restaurantId, String estado);
    List<ReservationEntity> findByRestaurant_Id(Long restaurantId);
     List<ReservationEntity> findByEmail(String email);
}

