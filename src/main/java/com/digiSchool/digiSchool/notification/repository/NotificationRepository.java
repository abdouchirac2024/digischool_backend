package com.digiSchool.digiSchool.notification.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.digiSchool.digiSchool.notification.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDestinataire_IdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByDestinataire_IdAndLuFalseOrderByCreatedAtDesc(Long userId);
    long countByDestinataire_IdAndLuFalse(Long userId);
}
