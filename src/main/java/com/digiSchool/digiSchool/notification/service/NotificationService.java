package com.digiSchool.digiSchool.notification.service;

import java.util.List;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.notification.dto.NotificationDto;
import com.digiSchool.digiSchool.notification.model.TypeNotification;

public interface NotificationService {
    void envoyerNotification(User destinataire, String titre, String message, TypeNotification type);
    List<NotificationDto> getNotificationsNonLues(Long userId);
    List<NotificationDto> getNotifications(Long userId);
    void marquerCommeLue(Long notificationId);
    void marquerToutesCommeLues(Long userId);
    long compterNonLues(Long userId);
}
