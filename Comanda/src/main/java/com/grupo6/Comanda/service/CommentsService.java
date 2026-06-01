package com.grupo6.Comanda.service;
 
import com.grupo6.Comanda.controller.dto.CommentsRequestDto;
import com.grupo6.Comanda.model.entities.CommentEntity;
import org.springframework.http.ResponseEntity;
 
import java.util.List;
import java.util.Map;
 
public interface CommentsService {
 
    List<CommentEntity> list(Long restaurantId, String tipo);
 
    CommentEntity submitDto(CommentsRequestDto dto);
 
    ResponseEntity<Map<String, Object>> markRead(Long id);
 
    ResponseEntity<Void> delete(Long id);
 
    Map<String, Long> unreadCount();
}
 