package com.grupo6.Comanda.model.entities;

import jakarta.persistence.*;

/**
 * Notificación en-app para un usuario.
 *
 * Tipos:
 *   - RESERVATION_CONFIRMED  → reserva confirmada por el restaurante
 *   - RESERVATION_CANCELLED  → reserva cancelada por el restaurante
 *   - COMMENT_REPLY          → el restaurante respondió un comentario del usuario
 */
@Entity
@Table(name = "notifications")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Email del usuario destinatario */
    @Column(name = "user_email", nullable = false)
    private String userEmail;

    /**
     * Tipo de notificación:
     * RESERVATION_CONFIRMED | RESERVATION_CANCELLED | COMMENT_REPLY
     */
    @Column(name = "tipo", nullable = false)
    private String tipo;

    /** Texto visible para el usuario */
    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "fecha", nullable = false)
    private String fecha;

    
    @Column(name = "leida", nullable = false)
    private Boolean leida = false;


    @Column(name = "reserva_id")
    private Long reservaId;

    
    @Column(name = "comentario_id")
    private Long comentarioId;

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public Boolean getLeida() { return leida; }
    public void setLeida(Boolean leida) { this.leida = leida; }

    public Long getReservaId() { return reservaId; }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }

    public Long getComentarioId() { return comentarioId; }
    public void setComentarioId(Long comentarioId) { this.comentarioId = comentarioId; }
}