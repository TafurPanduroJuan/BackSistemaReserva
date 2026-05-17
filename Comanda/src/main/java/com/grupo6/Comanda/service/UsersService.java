package com.grupo6.Comanda.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import com.grupo6.Comanda.model.entities.UserEntity;

public interface UsersService {
    
    List<UserEntity> listAll();

    ResponseEntity<UserEntity> getOne(Long id);
    ResponseEntity<Map<String, Object>> changeRole(Long id, Map<String, String> body);
    ResponseEntity<Void> delete(Long id);
    ResponseEntity<UserEntity> getMe(UserDetails principal);
    ResponseEntity<UserEntity> updateMe(UserDetails principal, Map<String, String> body);
}
