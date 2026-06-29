package com.grupo6.Comanda.repository;

import com.grupo6.Comanda.model.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {


    List<NotificationEntity> findByUserEmailOrderByFechaDesc(String userEmail);
    List<NotificationEntity> findByUserEmailAndLeidaFalseOrderByFechaDesc(String userEmail);
    Long countByUserEmailAndLeidaFalse(String userEmail);

    @Modifying
    @Transactional
    @Query("UPDATE NotificationEntity n SET n.leida = true WHERE n.userEmail = :email AND n.leida = false")
    void markAllReadByEmail(String email);
}