package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.model.entities.CommentEntity;
import com.grupo6.Comanda.repository.CommentRepository;
import com.grupo6.Comanda.service.CommentsService;
import com.grupo6.Comanda.service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Endpoints used by the React frontend:
 *   GET    /api/comments                        → list all (admin)
 *   GET    /api/comments?restaurantId=&tipo=    → filtered list
 *   POST   /api/comments                        → submit (public, from Form.jsx)
 *   PUT    /api/comments/{id}/read              → mark as read (admin)
 *   DELETE /api/comments/{id}                   → archive/delete (admin)
 *   GET    /api/comments/unread-count           → badge count (admin dashboard)
 *   POST   /api/comments/{id}/reply             → responder comentario (admin/personal)
 *   GET    /api/comments/me                     → comentarios del usuario autenticado (con y sin respuesta)
 *   GET    /api/comments/me/replies             → solo comentarios con respuesta del restaurante
 */
@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class CommentsApiController {

    private final CommentsService commentsService;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    public CommentsApiController(CommentsService commentsService,
                                  CommentRepository commentRepository,
                                  NotificationService notificationService) {
        this.commentsService = commentsService;
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<CommentEntity>> list(
            @RequestParam(value = "restaurantId", required = false) Long restaurantId,
            @RequestParam(value = "tipo", required = false) String tipo) {
        return ResponseEntity.ok(commentsService.list(restaurantId, tipo));
    }

    /**
     * Comentarios del usuario autenticado (todos).
     */
    @GetMapping("/me")
    public ResponseEntity<List<CommentEntity>> myComments(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(commentRepository.findByEmail(email));
    }

    /**
     * Solo comentarios del usuario que ya tienen respuesta del restaurante.
     */
    @GetMapping("/me/replies")
    public ResponseEntity<List<CommentEntity>> myReplies(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(
            commentRepository.findByEmailAndRespuestaRestauranteIsNotNull(email)
        );
    }

    /**
     * Public: accepts CommentEntity directly.
     */
    @PostMapping
    public ResponseEntity<CommentEntity> submit(@RequestBody CommentEntity comment) {
        return ResponseEntity.ok(commentsService.submit(comment));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, Object>> markRead(@PathVariable Long id) {
        return commentsService.markRead(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return commentsService.delete(id);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount() {
        return ResponseEntity.ok(commentsService.unreadCount());
    }

    /**
     * Responder a un comentario (admin / personal del restaurante).
     * Body: { "respuesta": "Texto de la respuesta del restaurante" }
     * Envía notificación por email al cliente.
     */
    @PostMapping("/{id}/reply")
    public ResponseEntity<Map<String, Object>> reply(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String respuesta = body.get("respuesta");
        if (respuesta == null || respuesta.isBlank()) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "El campo 'respuesta' es requerido"));
        }

        return commentRepository.findById(id)
            .map(comentario -> {
                comentario.setRespuestaRestaurante(respuesta);
                comentario.setFechaRespuesta(LocalDate.now().toString());
                comentario.setLeido(true);
                commentRepository.save(comentario);

                // Notificar al cliente por email
                notificationService.notificarRespuestaComentario(comentario);

                return ResponseEntity.ok(Map.<String, Object>of(
                    "message", "Respuesta guardada y notificación enviada",
                    "id", id,
                    "respuesta", respuesta,
                    "fechaRespuesta", comentario.getFechaRespuesta()
                ));
            })
            .orElseGet(() -> ResponseEntity.<Map<String, Object>>notFound().build());
    }
}