package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.model.entities.CommentEntity;
import com.grupo6.Comanda.service.CommentsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
 */
@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class CommentsApiController {

    private final CommentsService commentsService;

    public CommentsApiController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping
    public ResponseEntity<List<CommentEntity>> list(
            @RequestParam(value = "restaurantId", required = false) Long restaurantId,
            @RequestParam(value = "tipo", required = false) String tipo) {
        return ResponseEntity.ok(commentsService.list(restaurantId, tipo));
    }

    /** Public: submitted from the contact/form page. No auth required. */
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
}