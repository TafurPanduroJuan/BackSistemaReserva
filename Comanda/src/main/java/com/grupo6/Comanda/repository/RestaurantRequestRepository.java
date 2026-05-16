package com.grupo6.Comanda.repository;

import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRequestRepository extends JpaRepository<RestaurantRequestEntity, Long> {
    List<RestaurantRequestEntity> findByEstado(String estado);
}