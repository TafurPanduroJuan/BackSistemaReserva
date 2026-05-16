package com.grupo6.Comanda.model.entities;

import com.grupo6.Comanda.model.enums.ReservationStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // SQLite: INTEGER PRIMARY KEY auto-increments
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @Column(name = "cliente", nullable = false)
    private String cliente;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "tel", nullable = false)
    private String tel;

    @Column(name = "fecha", nullable = false)
    private String fecha;

    @Column(name = "hora", nullable = false)
    private String hora;

    @Column(name = "personas", nullable = false)
    private Integer personas;

    @Column(name = "mesa_numero", nullable = false)
    private Integer mesaNumero;

    @Column(name = "zona", nullable = false)
    private String zona;

    @Column(name = "notas")
    private String notas;

   /**
     * SQLite guarda esto como texto. 
     * El esquema usa valores en minúscula (pendiente, confirmada, cancelada, cancelada_cliente). 
     * Usamos un convertidor para que el enum en Java quede en mayúscula y en la BD en minúscula.
    */

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

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

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
