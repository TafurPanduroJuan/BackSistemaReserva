package com.grupo6.Comanda.model.entities;


import jakarta.persistence.*;

@Entity
@Table(name="tables", uniqueConstraints ={
    @UniqueConstraint(name="uk_tables_restaurant_numero", columnNames = {"restaurant_id", "numero"})
})
public class TableEntity {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    @Column(name = "estado", nullable = false)
    private String estado; // disponible | reservada

    @Column(name = "zona", nullable = false)
    private String zona; // Terraza | Salón Interior | VIP

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RestaurantEntity getRestaurant() { return restaurant; }
    public void setRestaurant(RestaurantEntity restaurant) { this.restaurant = restaurant; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getZona() { return zona; }
    public void setZona(String zona) { this.zona = zona; }
}
