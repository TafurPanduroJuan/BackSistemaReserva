package com.grupo6.Comanda.service.impl;

import com.grupo6.Comanda.auth.dto.AuthDtos.UpdateMeRequest;
import com.grupo6.Comanda.model.entities.UserEntity;
import com.grupo6.Comanda.model.enums.UserRole;
import com.grupo6.Comanda.repository.UserRepository;
import com.grupo6.Comanda.service.UsersService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;

    public UsersServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> listAll() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity<UserEntity> getOne(Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Map<String, Object>> changeRole(Long id, Map<String, String> body) {
        return userRepository.findById(id).map(user -> {
            String rolStr = body.get("rol");
            if (rolStr != null) {
                try {
                    user.setRole(UserRole.valueOf(rolStr.toUpperCase()));
                } catch (IllegalArgumentException ignored) {}
            }
            user.setRestaurant(body.get("restaurante"));
            userRepository.save(user);
            return ResponseEntity.ok(Map.<String, Object>of("message", "Role updated", "id", id));
        }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
       if(!userRepository.existsById(id)) return ResponseEntity.notFound().build();
       userRepository.deleteById(id);
       return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserEntity> getMe(UserDetails principal) {
       if (principal == null) return ResponseEntity.status(401).build();
       return userRepository.findByEmail(principal.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Override
    public ResponseEntity<UserEntity> updateMe(UserDetails principal, UpdateMeRequest body) {
        if (principal == null) return ResponseEntity.status(401).build();
        if (body == null)      return ResponseEntity.badRequest().build();

        return userRepository.findByEmail(principal.getUsername()).map(user -> {

            // nombre
            if (body.getNombre() != null && !body.getNombre().isBlank()) {
                user.setName(body.getNombre().trim());
            }

            // avatar
            if (body.getAvatar() != null && !body.getAvatar().isBlank()) {
                user.setAvatar(body.getAvatar().trim());
            }

            // telefono — validar 9 dígitos exactos si viene informado
            if (body.getTelefono() != null) {
                String telStr = String.valueOf(body.getTelefono());
                if (!telStr.matches("\\d{9}")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "El teléfono debe tener exactamente 9 dígitos numéricos");
                }
                user.setTelefono(body.getTelefono());
            }

            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }
}