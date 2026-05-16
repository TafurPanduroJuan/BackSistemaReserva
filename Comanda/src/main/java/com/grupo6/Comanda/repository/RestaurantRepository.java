package com.grupo6.Comanda.repository;

import com.grupo6.Comanda.model.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    // Field in entity is "nombre", not "name"
    Optional<RestaurantEntity> findByNombre(String nombre);
}
