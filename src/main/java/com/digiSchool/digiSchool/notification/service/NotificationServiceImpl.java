package com.digiSchool.digiSchool.notification.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.notification.dto.NotificationDto;
import com.digiSchool.digiSchool.notification.model.Notification;
import com.digiSchool.digiSchool.notification.model.TypeNotification;
import com.digiSchool.digiSchool.notification.repository.NotificationRepository;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void envoyerNotification(User destinataire, String titre, String message, TypeNotification type) {
        Notification notification = new Notification();
        notification.setDestinataire(destinataire);
        notification.setTitre(titre);
        notification.setMessage(message);
        notification.setType(type);
        notification.setLu(false);
        Notification saved = notificationRepository.save(notification);

        // Push WebSocket temps réel — non bloquant si WebSocket indisponible
        try {
            messagingTemplate.convertAndSendToUser(
                destinataire.getEmail(),
                "/queue/notifications",
                toDto(saved)
            );
        } catch (Exception e) {
            log.warn("[Notification] WebSocket push failed for {}: {}", destinataire.getEmail(), e.getMessage());
        }
    }

    @Override
    public List<NotificationDto> getNotificationsNonLues(Long userId) {
        return notificationRepository.findByDestinataire_IdAndLuFalseOrderByCreatedAtDesc(userId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<NotificationDto> getNotifications(Long userId) {
        return notificationRepository.findByDestinataire_IdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public void marquerCommeLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));
        notification.setLu(true);
        notificationRepository.save(notification);
    }

    @Override
    public void marquerToutesCommeLues(Long userId) {
        List<Notification> unread = notificationRepository.findByDestinataire_IdAndLuFalseOrderByCreatedAtDesc(userId);
        unread.forEach(n -> n.setLu(true));
        notificationRepository.saveAll(unread);
    }

    @Override
    public long compterNonLues(Long userId) {
        return notificationRepository.countByDestinataire_IdAndLuFalse(userId);
    }

    private NotificationDto toDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setTitre(notification.getTitre());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setLu(notification.getLu());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }
}
