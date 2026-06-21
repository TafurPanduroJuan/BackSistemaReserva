package com.grupo6.Comanda.model.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    @Column(name = "usuario", nullable = false)
    private String usuario;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "tipo", nullable = false)
    private String tipo; // comentario | reclamo | experiencia

    @Column(name = "asunto", nullable = false)
    private String asunto;

    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    @Column(name = "fecha", nullable = false)
    private String fecha;

    @Column(name = "calificacion")
    private Integer calificacion;

    /**
     * SQLite stores booleans as 0/1 INTEGER.
     */
    @Column(name = "leido", nullable = false)
    private Boolean leido;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RestaurantEntity getRestaurant() { return restaurant; }
    public void setRestaurant(RestaurantEntity restaurant) { this.restaurant = restaurant; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }

    @Column(name = "respuesta_restaurante", columnDefinition = "TEXT")
    private String respuestaRestaurante;

    @Column(name = "fecha_respuesta")
    private String fechaRespuesta;

    public String getRespuestaRestaurante() { return respuestaRestaurante; }
    public void setRespuestaRestaurante(String respuestaRestaurante) { this.respuestaRestaurante = respuestaRestaurante; }

    public String getFechaRespuesta() { return fechaRespuesta; }
    public void setFechaRespuesta(String fechaRespuesta) { this.fechaRespuesta = fechaRespuesta; }
}