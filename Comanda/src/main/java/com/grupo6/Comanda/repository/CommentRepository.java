package com.grupo6.Comanda.repository;

import com.grupo6.Comanda.model.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByRestaurant_Id(Long restaurantId);
    List<CommentEntity> findByRestaurant_IdAndTipo(Long restaurantId, String tipo);
    List<CommentEntity> findByTipo(String tipo);
    Long countByLeidoFalse();

    /** Para el perfil del usuario: comentarios con respuesta del restaurante */
    List<CommentEntity> findByEmailAndRespuestaRestauranteIsNotNull(String email);

    /** Para el perfil: todos los comentarios del usuario */
    List<CommentEntity> findByEmail(String email);
}