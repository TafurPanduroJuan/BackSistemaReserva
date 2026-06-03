package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.model.entities.TableEntity;
import com.grupo6.Comanda.service.TablesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin
public class TablesApiController {

    private final TablesService tablesService;

    public TablesApiController(TablesService tablesService) {
        this.tablesService = tablesService;
    }

    // ── DTO interno ──────────────────────────────────────────────────────────

    @Schema(description = "Datos para reservar una mesa")
    public static class ReserveTableRequest {

        @Schema(description = "ID del restaurante", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        private Long restaurantId;

        @Schema(description = "Número de mesa a reservar", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
        private Integer mesaNumero;

        @Schema(description = "Zona del restaurante: Terraza | Salón Interior | VIP", example = "Salón Interior")
        private String zona;

        @Schema(description = "Nombre del cliente", example = "Ricardo Palma", requiredMode = Schema.RequiredMode.REQUIRED)
        private String cliente;

        @Schema(description = "Correo del cliente", example = "ricardo@test.com", requiredMode = Schema.RequiredMode.REQUIRED)
        private String email;

        @Schema(description = "Teléfono del cliente (exactamente 9 dígitos)", example = "987777888")
        private Long tel;

        @Schema(description = "Fecha de la reserva (YYYY-MM-DD)", example = "2026-07-20", requiredMode = Schema.RequiredMode.REQUIRED)
        private String fecha;

        @Schema(description = "Hora de la reserva (HH:mm)", example = "20:00", requiredMode = Schema.RequiredMode.REQUIRED)
        private String hora;

        @Schema(description = "Número de personas", example = "3")
        private Integer personas;

        @Schema(description = "Notas adicionales del cliente", example = "Sin cebolla en todos los platos")
        private String notas;

        public Long    getRestaurantId()          { return restaurantId; }
        public void    setRestaurantId(Long v)    { this.restaurantId = v; }
        public Integer getMesaNumero()            { return mesaNumero; }
        public void    setMesaNumero(Integer v)   { this.mesaNumero = v; }
        public String  getZona()                  { return zona; }
        public void    setZona(String v)          { this.zona = v; }
        public String  getCliente()               { return cliente; }
        public void    setCliente(String v)       { this.cliente = v; }
        public String  getEmail()                 { return email; }
        public void    setEmail(String v)         { this.email = v; }
        public Long    getTel()                   { return tel; }
        public void    setTel(Long v)             { this.tel = v; }
        public String  getFecha()                 { return fecha; }
        public void    setFecha(String v)         { this.fecha = v; }
        public String  getHora()                  { return hora; }
        public void    setHora(String v)          { this.hora = v; }
        public Integer getPersonas()              { return personas; }
        public void    setPersonas(Integer v)     { this.personas = v; }
        public String  getNotas()                 { return notas; }
        public void    setNotas(String v)         { this.notas = v; }

        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("restaurantId", restaurantId);
            map.put("tableNumero",  mesaNumero);   
            if (zona     != null) map.put("zona",     zona);
            if (cliente  != null) map.put("cliente",  cliente);
            if (email    != null) map.put("email",    email);
            if (tel      != null) map.put("tel",      tel);
            if (fecha    != null) map.put("fecha",    fecha);
            if (hora     != null) map.put("hora",     hora);
            if (personas != null) map.put("personas", personas);
            if (notas    != null) map.put("notas",    notas);
            return map;
        }
    }

    // ── Endpoints ────────────────────────────────────────────────────────────

    @Operation(summary = "Crear mesa",
               description = "Crea una nueva mesa para el restaurante. Requiere rol PERSONAL o ADMINISTRADOR.")
    @PostMapping
    public ResponseEntity<TableEntity> createTable(
            @Parameter(description = "ID del restaurante", required = true, example = "1")
            @RequestParam("restaurantId") Long restaurantId,
            @RequestBody TableEntity table) {
        return ResponseEntity.ok(tablesService.createTable(restaurantId, table));
    }

    @Operation(summary = "Listar mesas del restaurante")
    @GetMapping
    public ResponseEntity<List<TableEntity>> getTablesByRestaurant(
            @Parameter(description = "ID del restaurante", required = true, example = "1")
            @RequestParam("restaurantId") Long restaurantId) {
        return ResponseEntity.ok(tablesService.getTablesByRestaurant(restaurantId));
    }

    @Operation(summary = "Mesas disponibles",
               description = "Retorna solo las mesas con estado 'disponible'. Filtra opcionalmente por zona.")
    @GetMapping("/available")
    public ResponseEntity<List<TableEntity>> getAvailableTables(
            @Parameter(description = "ID del restaurante", required = true, example = "1")
            @RequestParam("restaurantId") Long restaurantId,
            @Parameter(description = "Zona: Terraza | Salón Interior | VIP")
            @RequestParam(value = "zona", required = false) String zona) {
        return ResponseEntity.ok(tablesService.getAvailableTables(restaurantId, zona));
    }

    @Operation(
        summary = "Reservar mesa",
        description = "Crea una reserva y cambia el estado de la mesa a 'reservada'. Endpoint público.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Reserva creada exitosamente",
                content = @Content(schema = @Schema(implementation = Map.class),
                    examples = @ExampleObject(value = "{\"message\": \"Reserved with id=5\"}")))
        }
    )
    @PostMapping("/reserve")
    public ResponseEntity<Map<String, Object>> reserveTable(
            @RequestBody ReserveTableRequest body) {
        String result = tablesService.reserveTable(body.toMap());
        return ResponseEntity.ok(Map.of("message", result));
    }

    @Operation(summary = "Eliminar mesa",
               description = "Elimina una mesa por su ID. Requiere rol PERSONAL o ADMINISTRADOR.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable Long id) {
        tablesService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}