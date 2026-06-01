package com.grupo6.Comanda.service.impl;

import com.grupo6.Comanda.model.entities.CommentEntity;
import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.repository.CommentRepository;
import com.grupo6.Comanda.repository.RestaurantRepository;
import com.grupo6.Comanda.service.CommentsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;
    private final RestaurantRepository restaurantRepository;

    public CommentsServiceImpl(CommentRepository commentRepository,
                               RestaurantRepository restaurantRepository) {
        this.commentRepository    = commentRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public List<CommentEntity> list(Long restaurantId, String tipo) {
        if (restaurantId != null && tipo != null && !tipo.isBlank()) {
            return commentRepository.findByRestaurant_IdAndTipo(restaurantId, tipo);
        }
        if (restaurantId != null) {
            return commentRepository.findByRestaurant_Id(restaurantId);
        }
        if (tipo != null && !tipo.isBlank()) {
            return commentRepository.findByTipo(tipo);
        }
        return commentRepository.findAll();
    }

    @Override
    public CommentEntity submit(CommentEntity incoming) {

        // Validar que venga restaurant con id
        if (incoming.getRestaurant() == null || incoming.getRestaurant().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "restaurant.id is required");
        }

        // Buscar el restaurante real en la BD por su id
        Long restaurantId = incoming.getRestaurant().getId();
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Restaurant not found with id: " + restaurantId));

        // Reemplazar el objeto restaurant del body por el real de la BD
        incoming.setRestaurant(restaurant);

        // Valores por defecto
        incoming.setLeido(false);
        if (incoming.getFecha() == null || incoming.getFecha().isBlank()) {
            incoming.setFecha(LocalDate.now().toString());
        }

        return commentRepository.save(incoming);
    }

    @Override
    public ResponseEntity<Map<String, Object>> markRead(Long id) {
        return commentRepository.findById(id).map(c -> {
            c.setLeido(true);
            commentRepository.save(c);
            return ResponseEntity.ok(Map.<String, Object>of("message", "Marked as read", "id", id));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        if (!commentRepository.existsById(id)) return ResponseEntity.notFound().build();
        commentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public Map<String, Long> unreadCount() {
        return Map.of("count", commentRepository.countByLeidoFalse());
    }
}