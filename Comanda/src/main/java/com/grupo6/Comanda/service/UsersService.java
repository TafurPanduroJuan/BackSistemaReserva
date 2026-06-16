package com.grupo6.Comanda.service;

import com.grupo6.Comanda.auth.dto.AuthDtos.UpdateMeRequest;
import com.grupo6.Comanda.model.entities.UserEntity;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface UsersService {

    List<UserEntity> listAll();

    ResponseEntity<UserEntity> getOne(Long id);

    ResponseEntity<Map<String, Object>> changeRole(Long id, Map<String, String> body);

    ResponseEntity<Void> delete(Long id);

    ResponseEntity<UserEntity> getMe(UserDetails principal);

    ResponseEntity<UserEntity> updateMe(UserDetails principal, UpdateMeRequest body);

    ResponseEntity<Map<String, Object>> resetPassword(Long id, Map<String, String> body);
}