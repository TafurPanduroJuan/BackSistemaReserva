package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.model.entities.TableEntity;
import com.grupo6.Comanda.service.TablesService;

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
    @PostMapping
    public ResponseEntity<TableEntity> createTable(
            @RequestParam("restaurantId") Long restaurantId,
            @RequestBody TableEntity table) {
        return ResponseEntity.ok(tablesService.createTable(restaurantId, table));
    }

    @GetMapping
    public ResponseEntity<List<TableEntity>> getTablesByRestaurant(
            @RequestParam("restaurantId") Long restaurantId) {
        return ResponseEntity.ok(tablesService.getTablesByRestaurant(restaurantId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<TableEntity>> getAvailableTables(
            @RequestParam("restaurantId") Long restaurantId,
            @RequestParam(value = "zona", required = false) String zona
    ) {
        return ResponseEntity.ok(tablesService.getAvailableTables(restaurantId, zona));
    }

    @PostMapping("/reserve")
    public ResponseEntity<Map<String, Object>> reserveTable(
            @RequestBody Map<String, Object> payload) {
        String result = tablesService.reserveTable(payload);
        return ResponseEntity.ok(Map.of("message", result));
    }
}