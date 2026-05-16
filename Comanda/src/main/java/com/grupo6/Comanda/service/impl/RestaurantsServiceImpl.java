package com.grupo6.Comanda.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.RestaurantRequestEntity;
import com.grupo6.Comanda.repository.RestaurantRepository;
import com.grupo6.Comanda.repository.RestaurantRequestRepository;
import com.grupo6.Comanda.service.RestaurantsService;

public class RestaurantsServiceImpl implements RestaurantsService{
    private final RestaurantRepository restaurantRepository;
    private final RestaurantRequestRepository requestRepository;

    public RestaurantsServiceImpl(RestaurantRepository restaurantRepository,
                                  RestaurantRequestRepository requestRepository) {
        this.restaurantRepository = restaurantRepository;
        this.requestRepository    = requestRepository;
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
            updated.setId(id);
            return ResponseEntity.ok(restaurantRepository.save(updated));
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listRequests'");
    }

    @Override
    public RestaurantRequestEntity submitRequest(RestaurantRequestEntity req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'submitRequest'");
    }

    @Override
    public ResponseEntity<Map<String, Object>> acceptRequest(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'acceptRequest'");
    }

    @Override
    public ResponseEntity<Map<String, Object>> rejectRequest(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rejectRequest'");
    }
    
}
