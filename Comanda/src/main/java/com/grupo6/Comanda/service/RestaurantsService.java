package com.grupo6.Comanda.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;

public interface RestaurantsService {
    
    List<RestaurantEntity> listAll();

    ResponseEntity<RestaurantEntity> getOne(Long id);

    RestaurantEntity create(RestaurantEntity restaurant);

    ResponseEntity<RestaurantEntity> update(Long id, RestaurantEntity updated);

    ResponseEntity<Void> delete(Long id);

    List<RestaurantRequestEntity> listRequests(String estado);

    RestaurantRequestEntity submitRequest(RestaurantRequestEntity req);

    ResponseEntity<Map<String, Object>> acceptRequest(Long id);

    ResponseEntity<Map<String, Object>> rejectRequest(Long id);

    ResponseEntity<RestaurantEntity> toggleCierre(Long id, Map<String, Object> body);
}
