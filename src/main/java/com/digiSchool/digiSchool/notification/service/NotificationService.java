package com.digiSchool.digiSchool.notification.service;

import java.util.List;

import com.digiSchool.digiSchool.notification.model.Notification;

public interface NotificationService {

    void creerNotification(Long destinataireId, String tenantId, String titre, String message, String type);

    List<Notification> getNotifications(Long userId);

    List<Notification> getUnreadNotifications(Long userId);

    void marquerLue(Long notificationId);

    long countUnread(Long userId);
}
