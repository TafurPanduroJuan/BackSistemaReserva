package com.grupo6.Comanda.service;

import com.grupo6.Comanda.model.entities.TableEntity;

import java.util.List;

public interface TablesService {

    List<TableEntity> getTablesByRestaurant(Long restaurantId);

    List<TableEntity> getAvailableTables(Long restaurantId, String zona);

    String reserveTable(java.util.Map<String, Object> payload);
}

