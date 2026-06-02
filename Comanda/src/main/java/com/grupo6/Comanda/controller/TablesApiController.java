package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.controller.dto.ReserveTableRequest;
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
}