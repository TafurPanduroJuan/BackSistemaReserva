package com.grupo6.Comanda.service.impl;

import com.grupo6.Comanda.controller.dto.CommentsRequestDto;
import com.grupo6.Comanda.model.entities.CommentEntity;
import com.grupo6.Comanda.repository.CommentRepository;
import com.grupo6.Comanda.repository.RestaurantRepository;
import com.grupo6.Comanda.service.CommentsService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    public CommentEntity submitDto(CommentsRequestDto dto) {
        CommentEntity comment = new CommentEntity();
        comment.setLeido(false);
        comment.setUsuario(dto.getUsuario());
        comment.setEmail(dto.getEmail());
        comment.setTelefono(dto.getTelefono());
        comment.setTipo(dto.getTipo());
        comment.setAsunto(dto.getAsunto());
        comment.setMensaje(dto.getMensaje());
        comment.setCalificacion(dto.getCalificacion());
        comment.setFecha(dto.getFecha() != null && !dto.getFecha().isBlank()
                ? dto.getFecha()
                : LocalDate.now().toString());

        if (dto.getRestaurantId() != null) {
            restaurantRepository.findById(dto.getRestaurantId())
                    .ifPresent(comment::setRestaurant);
        }

        return commentRepository.save(comment);
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