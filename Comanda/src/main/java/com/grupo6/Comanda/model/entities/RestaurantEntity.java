package com.grupo6.Comanda.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Schema(description = "Dirección exacta", example = "Av. Larco 456, Miraflores")
    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    @Schema(description = "Mensaje de bienvenida personalizado", example = "¡Te esperamos para una experiencia única!")
    @JsonProperty("mensaje_personalizado")
    @Column(name = "mensaje_personalizado", columnDefinition = "TEXT")
    private String mensajePersonalizado;

    @Schema(description = "Número total de mesas disponibles", example = "20")
    @Column(name = "mesas")
    private Integer mesas;

    @Schema(description = "Rango de precios del restaurante: $ (económico), $$ (moderado), $$$ (elevado) o $$$$ (premium)", example = "$$")
    @Column(name = "precio")
    private String precio = "$";

    @Schema(description = "Teléfono del restaurante (exactamente 9 dígitos)", example = "987654321")
    @Column(name = "telefono")
    private Long telefono;

    @Schema(description = "Correo electrónico de contacto", example = "contacto@labellaitalia.pe")
    @Column(name = "email")
    private String email;

    @Schema(description = "URL de la imagen del restaurante")
    @Column(name = "imagen", columnDefinition = "TEXT")
    private String imagen;

    // ── Horarios por día ──────────────────────────────────────────────────────
    // Formato de cada campo: "HH:mm-HH:mm"  (apertura-cierre)
    // Ejemplo: "09:00-22:00". Si el restaurante no abre ese día → null.

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

    // ── Cierre temporal ───────────────────────────────────────────────────────

    @Schema(description = "Indica si el restaurante está cerrado hoy por algún inconveniente", example = "false")
    @Column(name = "cerrado_hoy")
    private Boolean cerradoHoy = false;

    @Schema(description = "Motivo del cierre temporal", example = "Mantenimiento de cocina")
    @Column(name = "motivo_cierre", columnDefinition = "TEXT")
    private String motivoCierre;

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public Long getId()             { return id; }
    public void setId(Long id)      { this.id = id; }

    public String getNombre()       { return nombre; }
    public void setNombre(String n) { this.nombre = n; }

    public String getTipo()         { return tipo; }
    public void setTipo(String t)   { this.tipo = t; }

    public String getDistrito()           { return distrito; }
    public void   setDistrito(String d)   { this.distrito = d; }

    public String getDireccion()          { return direccion; }
    public void   setDireccion(String d)  { this.direccion = d; }

    public String getMensajePersonalizado()          { return mensajePersonalizado; }
    public void   setMensajePersonalizado(String m)  { this.mensajePersonalizado = m; }

    public Integer getMesas()             { return mesas; }
    public void    setMesas(Integer m)    { this.mesas = m; }

    public String getPrecio()             { return precio; }
    public void   setPrecio(String p)     { this.precio = p; }

    public Long getTelefono()             { return telefono; }
    public void setTelefono(Long t)       { this.telefono = t; }

    public String getEmail()              { return email; }
    public void   setEmail(String e)      { this.email = e; }

    public String getImagen()             { return imagen; }
    public void   setImagen(String i)     { this.imagen = i; }

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

    public Boolean getCerradoHoy()                { return cerradoHoy; }
    public void    setCerradoHoy(Boolean b)       { this.cerradoHoy = b; }

    public String getMotivoCierre()               { return motivoCierre; }
    public void   setMotivoCierre(String m)       { this.motivoCierre = m; }
}
