package com.grupo6.Comanda.repository;

import com.grupo6.Comanda.model.entities.TableEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<TableEntity, Long> {
    List<TableEntity> findByRestaurant_Id(Long restaurantId);
    java.util.Optional<TableEntity> findByRestaurant_IdAndNumero(Long restaurantId, Integer numero);

}

