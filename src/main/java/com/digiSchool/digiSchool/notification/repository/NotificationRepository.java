package com.digiSchool.digiSchool.notification.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.digiSchool.digiSchool.notification.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // @Query JPQL explicite pour éviter les interactions du @Filter tenantFilter
    // sur l'entité User lors de la génération de requêtes dérivées

    @Query("SELECT n FROM Notification n WHERE n.destinataire.id = :userId ORDER BY n.createdAt DESC")
    List<Notification> findByDestinaireId(@Param("userId") Long userId);

    @Query("SELECT n FROM Notification n WHERE n.destinataire.id = :userId AND n.lu = false ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByDestinaireId(@Param("userId") Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.destinataire.id = :userId AND n.lu = false")
    long countUnreadByDestinaireId(@Param("userId") Long userId);
}
