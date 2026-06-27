package com.grupo6.Comanda.repository;

import com.grupo6.Comanda.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByGoogleEmail(String googleEmail);
    Optional<UserEntity> findByPasswordResetToken(String token);
}

