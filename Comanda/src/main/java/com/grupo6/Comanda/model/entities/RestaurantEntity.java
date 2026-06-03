package com.grupo6.Comanda.model.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Schema(description = "Datos de un restaurante registrado en la plataforma")
@Entity
@Table(name = "restaurants")
public class RestaurantEntity {

    @Schema(description = "ID interno del restaurante", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Schema(description = "Nombre del restaurante", example = "La Bella Italia")
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Schema(description = "Tipo de cocina", example = "Italiana")
    @Column(name = "tipo")
    private String tipo;

    @Schema(description = "Distrito donde se ubica", example = "Miraflores")
    @Column(name = "distrito")
    private String distrito;

    // FIX: direccion puede ser larga (nombre de calle + número + referencia)
    @Schema(description = "Dirección exacta", example = "Av. Larco 456, Miraflores")
    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    // FIX: mensaje personalizado puede superar 255 chars (slogan, descripción)
    @Schema(description = "Mensaje de bienvenida personalizado", example = "¡Te esperamos para una experiencia única!")
    @Column(name = "mensaje_personalizado", columnDefinition = "TEXT")
    private String mensajePersonalizado;

    @Schema(description = "Número total de mesas disponibles", example = "20")
    @Column(name = "mesas")
    private Integer mesas;

    @Schema(description = "Teléfono del restaurante (exactamente 9 dígitos)", example = "987654321")
    @Column(name = "telefono")
    private Long telefono;

    @Schema(description = "Correo electrónico de contacto", example = "contacto@labellaitalia.pe")
    @Column(name = "email")
    private String email;

    // FIX: imagen es una URL que puede superar 255 chars (URLs de CDN/Cloudinary son largas)
    @Schema(description = "URL de la imagen del restaurante", example = "https://storage.example.com/img/bella-italia.jpg")
    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen;

    @Schema(description = "Hora de apertura", example = "12:00")
    @Column(name = "horario_apertura")
    private String horarioApertura;

    @Schema(description = "Hora de cierre", example = "23:00")
    @Column(name = "horario_cierre")
    private String horarioCierre;

    public Long   getId()           { return id; }
    public void   setId(Long id)    { this.id = id; }

    public String getNombre()       { return nombre; }
    public void   setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo()         { return tipo; }
    public void   setTipo(String tipo) { this.tipo = tipo; }

    public String getDistrito()     { return distrito; }
    public void   setDistrito(String distrito) { this.distrito = distrito; }

    public String getDireccion()    { return direccion; }
    public void   setDireccion(String direccion) { this.direccion = direccion; }

    public String getMensajePersonalizado() { return mensajePersonalizado; }
    public void   setMensajePersonalizado(String msg) { this.mensajePersonalizado = msg; }

    public Integer getMesas()       { return mesas; }
    public void    setMesas(Integer mesas) { this.mesas = mesas; }

    public Long   getTelefono()     { return telefono; }
    public void   setTelefono(Long telefono) { this.telefono = telefono; }

    public String getEmail()        { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getImagen()       { return imagen; }
    public void   setImagen(String imagen) { this.imagen = imagen; }

    public String getHorarioApertura() { return horarioApertura; }
    public void   setHorarioApertura(String h) { this.horarioApertura = h; }

    public String getHorarioCierre()   { return horarioCierre; }
    public void   setHorarioCierre(String h) { this.horarioCierre = h; }
}