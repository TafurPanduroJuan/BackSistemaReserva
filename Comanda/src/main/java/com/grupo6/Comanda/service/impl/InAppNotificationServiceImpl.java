package com.grupo6.Comanda.service.impl;

import com.grupo6.Comanda.model.entities.NotificationEntity;
import com.grupo6.Comanda.repository.NotificationRepository;
import com.grupo6.Comanda.service.InAppNotificationService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InAppNotificationServiceImpl implements InAppNotificationService {

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final NotificationRepository repo;

    public InAppNotificationServiceImpl(NotificationRepository repo) {
        this.repo = repo;
    }

    @Override
    public NotificationEntity crear(String userEmail, String tipo, String mensaje,
                                     Long reservaId, Long comentarioId) {
        NotificationEntity n = new NotificationEntity();
        n.setUserEmail(userEmail);
        n.setTipo(tipo);
        n.setMensaje(mensaje);
        n.setFecha(LocalDateTime.now().format(FMT));
        n.setLeida(false);
        n.setReservaId(reservaId);
        n.setComentarioId(comentarioId);
        return repo.save(n);
    }

    @Override
    public List<NotificationEntity> listar(String userEmail) {
        List<NotificationEntity> todas =
                repo.findByUserEmailOrderByFechaDesc(userEmail);
        // Limitar a 50 más recientes
        return todas.size() > 50 ? todas.subList(0, 50) : todas;
    }

    @Override
    public long contarNoLeidas(String userEmail) {
        return repo.countByUserEmailAndLeidaFalse(userEmail);
    }

    @Override
    public void marcarTodasLeidas(String userEmail) {
        repo.markAllReadByEmail(userEmail);
    }

    @Override
    public void marcarLeida(Long id) {
        repo.findById(id).ifPresent(n -> {
            n.setLeida(true);
            repo.save(n);
        });
    }
}