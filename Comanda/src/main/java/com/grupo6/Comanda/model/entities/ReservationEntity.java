package com.grupo6.Comanda.model.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Schema(description = "Reserva de mesa en un restaurante")
@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Schema(description = "ID de la reserva", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Schema(description = "Restaurante al que pertenece la reserva")
    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @Schema(description = "Nombre del cliente", example = "Ana Torres")
    @Column(name = "cliente", nullable = false)
    private String cliente;

    @Schema(description = "Correo electrónico del cliente", example = "ana@example.com")
    @Column(name = "email", nullable = false)
    private String email;

    
    @Schema(description = "Teléfono del cliente (exactamente 9 dígitos)", example = "987654321")
    @Column(name = "tel", nullable = false)
    private Long tel;

    @Schema(description = "Fecha de la reserva (YYYY-MM-DD)", example = "2026-07-15")
    @Column(name = "fecha", nullable = false)
    private String fecha;

    @Schema(description = "Hora de la reserva (HH:mm)", example = "19:30")
    @Column(name = "hora", nullable = false)
    private String hora;

    @Schema(description = "Número de personas", example = "4")
    @Column(name = "personas", nullable = false)
    private Integer personas;

    @Schema(description = "Número de mesa asignada", example = "5")
    @Column(name = "mesa_numero", nullable = false)
    private Integer mesaNumero;

    @Schema(description = "Zona del restaurante (interior / exterior / terraza)", example = "interior")
    @Column(name = "zona", nullable = false)
    private String zona;

    @Schema(description = "Notas adicionales del cliente", example = "Celebración de cumpleaños")
    @Column(name = "notas")
    private String notas;

    @Schema(description = "Estado de la reserva: pendiente | confirmada | cancelada | cancelada_cliente", example = "pendiente")
    @Column(name = "estado", nullable = false)
    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RestaurantEntity getRestaurant() { return restaurant; }
    public void setRestaurant(RestaurantEntity restaurant) { this.restaurant = restaurant; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getTel() { return tel; }
    public void setTel(Long tel) { this.tel = tel; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public Integer getPersonas() { return personas; }
    public void setPersonas(Integer personas) { this.personas = personas; }

    public Integer getMesaNumero() { return mesaNumero; }
    public void setMesaNumero(Integer mesaNumero) { this.mesaNumero = mesaNumero; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}