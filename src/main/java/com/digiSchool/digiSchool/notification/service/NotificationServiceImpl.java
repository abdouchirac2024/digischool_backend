package com.digiSchool.digiSchool.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digiSchool.digiSchool.notification.model.Notification;
import com.digiSchool.digiSchool.notification.repository.NotificationRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void creerNotification(Long destinataireId, String tenantId, String titre, String message, String type) {
        Notification notification = new Notification();
        notification.setDestinataireId(destinataireId);
        notification.setTenantId(tenantId);
        notification.setTitre(titre);
        notification.setMessage(message);
        notification.setType(type);
        notification.setLue(false);
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findByDestinataireIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByDestinataireIdAndLueFalse(userId);
    }

    @Override
    public void marquerLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));
        notification.setLue(true);
        notificationRepository.save(notification);
    }

    @Override
    public void marquerToutesLues(Long userId) {
        List<Notification> nonLues = notificationRepository.findByDestinataireIdAndLueFalse(userId);
        for (Notification n : nonLues) {
            n.setLue(true);
        }
        notificationRepository.saveAll(nonLues);
    }

    @Override
    public long countUnread(Long userId) {
        return notificationRepository.countByDestinataireIdAndLueFalse(userId);
    }
}
