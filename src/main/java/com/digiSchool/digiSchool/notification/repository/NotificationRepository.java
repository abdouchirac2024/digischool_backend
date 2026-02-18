package com.digiSchool.digiSchool.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.notification.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByDestinataireIdAndLueFalse(Long destinataireId);

    List<Notification> findByDestinataireIdOrderByCreatedAtDesc(Long destinataireId);

    long countByDestinataireIdAndLueFalse(Long destinataireId);
}
