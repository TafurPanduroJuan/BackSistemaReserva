package com.grupo6.Comanda.security.service;

import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.UserEntity;
import com.grupo6.Comanda.repository.RestaurantRepository;
import com.grupo6.Comanda.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("restaurantSecurityService")
public class RestaurantSecurityService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantSecurityService(RestaurantRepository restaurantRepository,
                                     UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * Devuelve true si el usuario autenticado (rol PERSONAL)
     * es el propietario del restaurante con el id dado.
     */
    
    public boolean esPropietario(Authentication authentication, Long restaurantId) {
        if (authentication == null || !authentication.isAuthenticated()) return false;

        String email = authentication.getName();

        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return false;

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) return false;

        return restaurant.getNombre() != null &&
               restaurant.getNombre().equalsIgnoreCase(user.getRestaurant());
    }
}