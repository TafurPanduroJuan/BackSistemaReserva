package com.grupo6.Comanda.model.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Schema(description = "Solicitud de registro de un nuevo restaurante")
@Entity
@Table(name = "restaurant_requests")
public class RestaurantRequestEntity {

    @Schema(description = "ID de la solicitud", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Schema(description = "Nombre del restaurante a registrar", example = "El Rincón Peruano")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Schema(description = "Nombre del propietario", example = "Carlos López")
    @Column(name = "propietario", nullable = false)
    private String propietario;

    @Schema(description = "Correo electrónico del propietario", example = "carlos@rinconperuano.pe")
    @Column(name = "email", nullable = false)
    private String email;

    @Schema(description = "Tipo de cocina", example = "Peruana")
    @Column(name = "tipo", nullable = false)
    private String tipo;

    @Schema(description = "Distrito donde se ubicará el restaurante", example = "Miraflores")
    @Column(name = "distrito", nullable = false)
    private String distrito;

    @Schema(description = "Dirección exacta del restaurante", example = "Av. Larco 123")
    @Column(name = "direccion")
    private String direccion;

    @Schema(description = "Teléfono de contacto (exactamente 9 dígitos)", example = "987654321")
    @Column(name = "telefono", nullable = false)
    private Long telefono;

    @Schema(description = "Descripción o mensaje adicional del solicitante", example = "Restaurante familiar con 10 años de experiencia")
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Schema(description = "Slogan o mensaje personalizado del restaurante", example = "El mejor sabor norteño")
    @Column(name = "mensaje_personalizado")
    private String mensajePersonalizado;

    @Schema(description = "Fecha de la solicitud (ISO-8601)", example = "2026-06-01", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "fecha", nullable = false)
    private String fecha;

    @Schema(description = "Estado de la solicitud: pendiente | aceptado | rechazado", example = "pendiente", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "estado", nullable = false)
    private String estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPropietario() { return propietario; }
    public void setPropietario(String propietario) { this.propietario = propietario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Long getTelefono() { return telefono; }
    public void setTelefono(Long telefono) { this.telefono = telefono; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getMensajePersonalizado() { return mensajePersonalizado; }
    public void setMensajePersonalizado(String mensajePersonalizado) { this.mensajePersonalizado = mensajePersonalizado; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}