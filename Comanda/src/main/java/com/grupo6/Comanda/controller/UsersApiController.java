package com.grupo6.Comanda.controller;

import com.grupo6.Comanda.model.entities.UserEntity;
import com.grupo6.Comanda.service.UsersService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Admin user management used by the intranet Usuarios.jsx page.
 *
 *   GET    /api/users        → list all users (admin)
 *   GET    /api/users/{id}   → get one
 *   PUT    /api/users/{id}/role  → change role (admin)
 *   DELETE /api/users/{id}   → delete user (admin)
 *   GET    /api/users/me     → profile for logged-in user
 *   PUT    /api/users/me     → update own profile (MiCuenta.jsx)
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UsersApiController {

    private final UsersService usersService;

    public UsersApiController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<List<UserEntity>> listAll() {
        return ResponseEntity.ok(usersService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getOne(@PathVariable Long id) {
        return usersService.getOne(id);
    }

    /**
     * Change a user's role and optionally their assigned restaurant.
     * Body: { "rol": "personal", "restaurante": "La Bella Italia" }
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<Map<String, Object>> changeRole(@PathVariable Long id,
                                                          @RequestBody Map<String, String> body) {
        return usersService.changeRole(id, body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return usersService.delete(id);
    }

    /** Returns the currently authenticated user's profile. */
    @GetMapping("/me")
    public ResponseEntity<UserEntity> me(@AuthenticationPrincipal UserDetails principal) {
        return usersService.getMe(principal);
    }

    /** Update own profile (nombre, avatar). */
    @PutMapping("/me")
    public ResponseEntity<UserEntity> updateMe(@AuthenticationPrincipal UserDetails principal,
                                               @RequestBody Map<String, String> body) {
        return usersService.updateMe(principal, body);
    }
}