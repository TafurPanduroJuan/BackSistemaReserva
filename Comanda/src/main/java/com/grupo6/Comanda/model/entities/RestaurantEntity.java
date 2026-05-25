package com.grupo6.Comanda.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurants")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Column(name = "distrito", nullable = false)
    private String distrito;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "mensaje_personalizado", nullable = false)
    private String mensajePersonalizado;

    @Column(name = "mesas", nullable = false)
    private Integer mesas;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "horario_apertura", nullable = false)
    private String horarioApertura;

    @Column(name = "horario_cierre", nullable = false)
    private String horarioCierre;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getMensajePersonalizado() { return mensajePersonalizado; }
    public void setMensajePersonalizado(String mensajePersonalizado) { this.mensajePersonalizado = mensajePersonalizado; }

    public Integer getMesas() { return mesas; }
    public void setMesas(Integer mesas) { this.mesas = mesas; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getHorarioApertura() { return horarioApertura; }
    public void setHorarioApertura(String horarioApertura) { this.horarioApertura = horarioApertura; }

    public String getHorarioCierre() { return horarioCierre; }
    public void setHorarioCierre(String horarioCierre) { this.horarioCierre = horarioCierre; }
}
