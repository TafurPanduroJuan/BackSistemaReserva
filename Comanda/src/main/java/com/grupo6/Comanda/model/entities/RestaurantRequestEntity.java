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
    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    @Schema(description = "Teléfono de contacto (exactamente 9 dígitos)", example = "987654321")
    @Column(name = "telefono", nullable = false)
    private Long telefono;

    @Schema(description = "Descripción o mensaje adicional del solicitante")
    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Schema(description = "Slogan o mensaje personalizado del restaurante", example = "El mejor sabor norteño")
    @Column(name = "mensaje_personalizado", columnDefinition = "TEXT")
    private String mensajePersonalizado;

    @Schema(description = "Imagen del restaurante en base64 o URL")
    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen;

    @Schema(description = "Rango de precios propuesto: $ (económico), $$ (moderado), $$$ (elevado) o $$$$ (premium)", example = "$$")
    @Column(name = "precio")
    private String precio;

    // ── Horarios por día ──────────────────────────────────────────────────────
    // Formato: "HH:mm-HH:mm"  (apertura-cierre). null = cerrado ese día.

    @Schema(description = "Horario del lunes (apertura-cierre)", example = "09:00-22:00")
    @Column(name = "horario_lunes")
    private String horarioLunes;

    @Schema(description = "Horario del martes (apertura-cierre)", example = "09:00-22:00")
    @Column(name = "horario_martes")
    private String horarioMartes;

    @Schema(description = "Horario del miércoles (apertura-cierre)", example = "09:00-22:00")
    @Column(name = "horario_miercoles")
    private String horarioMiercoles;

    @Schema(description = "Horario del jueves (apertura-cierre)", example = "09:00-22:00")
    @Column(name = "horario_jueves")
    private String horarioJueves;

    @Schema(description = "Horario del viernes (apertura-cierre)", example = "09:00-22:00")
    @Column(name = "horario_viernes")
    private String horarioViernes;

    @Schema(description = "Horario del sábado (apertura-cierre)", example = "10:00-23:00")
    @Column(name = "horario_sabado")
    private String horarioSabado;

    @Schema(description = "Horario del domingo (apertura-cierre)", example = "10:00-23:00")
    @Column(name = "horario_domingo")
    private String horarioDomingo;

    @Schema(description = "Fecha de la solicitud (ISO-8601)", example = "2026-06-01", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "fecha", nullable = false)
    private String fecha;

    @Schema(description = "Estado de la solicitud: pendiente | aceptado | rechazado", example = "pendiente", accessMode = Schema.AccessMode.READ_ONLY)
    @Column(name = "estado", nullable = false)
    private String estado;

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public Long getId()               { return id; }
    public void setId(Long id)        { this.id = id; }

    public String getNombre()         { return nombre; }
    public void setNombre(String n)   { this.nombre = n; }

    public String getPropietario()           { return propietario; }
    public void   setPropietario(String p)   { this.propietario = p; }

    public String getEmail()          { return email; }
    public void   setEmail(String e)  { this.email = e; }

    public String getTipo()           { return tipo; }
    public void   setTipo(String t)   { this.tipo = t; }

    public String getDistrito()           { return distrito; }
    public void   setDistrito(String d)   { this.distrito = d; }

    public String getDireccion()          { return direccion; }
    public void   setDireccion(String d)  { this.direccion = d; }

    public Long   getTelefono()           { return telefono; }
    public void   setTelefono(Long t)     { this.telefono = t; }

    public String getDescripcion()            { return descripcion; }
    public void   setDescripcion(String d)    { this.descripcion = d; }

    public String getMensajePersonalizado()           { return mensajePersonalizado; }
    public void   setMensajePersonalizado(String m)   { this.mensajePersonalizado = m; }

    public String getImagen()             { return imagen; }
    public void   setImagen(String i)     { this.imagen = i; }

    public String getPrecio()             { return precio; }
    public void   setPrecio(String p)     { this.precio = p; }

    public String getHorarioLunes()               { return horarioLunes; }
    public void   setHorarioLunes(String h)       { this.horarioLunes = h; }

    public String getHorarioMartes()              { return horarioMartes; }
    public void   setHorarioMartes(String h)      { this.horarioMartes = h; }

    public String getHorarioMiercoles()           { return horarioMiercoles; }
    public void   setHorarioMiercoles(String h)   { this.horarioMiercoles = h; }

    public String getHorarioJueves()              { return horarioJueves; }
    public void   setHorarioJueves(String h)      { this.horarioJueves = h; }

    public String getHorarioViernes()             { return horarioViernes; }
    public void   setHorarioViernes(String h)     { this.horarioViernes = h; }

    public String getHorarioSabado()              { return horarioSabado; }
    public void   setHorarioSabado(String h)      { this.horarioSabado = h; }

    public String getHorarioDomingo()             { return horarioDomingo; }
    public void   setHorarioDomingo(String h)     { this.horarioDomingo = h; }

    public String getFecha()              { return fecha; }
    public void   setFecha(String f)      { this.fecha = f; }

    public String getEstado()             { return estado; }
    public void   setEstado(String e)     { this.estado = e; }
}
