package com.grupo6.Comanda.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.grupo6.Comanda.model.entities.ReservationEntity;
import com.grupo6.Comanda.model.entities.RestaurantEntity;
import com.grupo6.Comanda.model.entities.TableEntity;
import com.grupo6.Comanda.repository.ReservationRepository;
import com.grupo6.Comanda.repository.RestaurantRepository;
import com.grupo6.Comanda.repository.TableRepository;
import com.grupo6.Comanda.service.TablesService;

public class TablesServiceImpl implements TablesService {
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;

    public TablesServiceImpl(TableRepository tableRepository,
                             ReservationRepository reservationRepository,
                             RestaurantRepository restaurantRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
    }


    @Override
    public List<TableEntity> getTablesByRestaurant(Long restaurantId) {
       return tableRepository.findByRestaurant_Id(restaurantId);
    }

    @Override
    public List<TableEntity> getAvailableTables(Long restaurantId, String zona) {
       List<TableEntity> all = tableRepository.findByRestaurant_Id(restaurantId);
       
       return all.stream()
       .filter(t -> "disponible".equalsIgnoreCase(t.getEstado()))
                .filter(t -> zona == null || zona.isBlank() || zona.equalsIgnoreCase(t.getZona()))
                .toList();
    }

    @Override
    public String reserveTable(Map<String, Object> payload) {
       if (payload == null) return "Missing payload";

        Object restaurantIdObj = payload.get("restaurantId");
        Object tableNumeroObj  = payload.get("tableNumero");

        if (restaurantIdObj == null || tableNumeroObj == null) {
            return "Missing restaurantId or tableNumero";
        }

        Long    restaurantId = ((Number) restaurantIdObj).longValue();
        Integer tableNumero  = ((Number) tableNumeroObj).intValue();

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));

        TableEntity table = tableRepository.findByRestaurant_IdAndNumero(restaurantId, tableNumero)
                .orElseThrow(() -> new IllegalArgumentException("Table not found: " + tableNumero));

        if (!"disponible".equalsIgnoreCase(table.getEstado())) {
            return "Table is not available";
        }

        String cliente  = (String) payload.getOrDefault("cliente", "");
        String email    = (String) payload.getOrDefault("email", "");
        String tel      = (String) payload.getOrDefault("tel", "");
        String fecha    = (String) payload.getOrDefault("fecha", LocalDate.now().toString());
        String hora     = (String) payload.getOrDefault("hora", "");
        Integer personas = payload.get("personas") instanceof Number
                ? ((Number) payload.get("personas")).intValue() : 0;
        String zona     = (String) payload.getOrDefault("zona", table.getZona());
        String notas    = (String) payload.getOrDefault("notas", null);

        ReservationEntity reservation = new ReservationEntity();
        reservation.setRestaurant(restaurant);
        reservation.setCliente(cliente);
        reservation.setEmail(email);
        reservation.setTel(tel);
        reservation.setFecha(fecha);
        reservation.setHora(hora);
        reservation.setPersonas(personas);
        reservation.setMesaNumero(tableNumero);
        reservation.setZona(zona);
        reservation.setNotas(notas);
        reservation.setEstado("pendiente"); // matches DB CHECK constraint

        ReservationEntity savedRes = reservationRepository.save(reservation);

        table.setEstado("reservada");
        tableRepository.save(table);

        return "Reserved with id=" + savedRes.getId();
    }
    
}
