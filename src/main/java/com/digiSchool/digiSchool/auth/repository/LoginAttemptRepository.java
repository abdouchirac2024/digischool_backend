package com.digiSchool.digiSchool.auth.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.auth.model.LoginAttempt;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    @Query("SELECT COUNT(la) FROM LoginAttempt la WHERE la.email = :email AND la.success = false AND la.attemptTime > :since")
    long countRecentFailedAttempts(@Param("email") String email, @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(la) FROM LoginAttempt la WHERE la.ipAddress = :ipAddress AND la.success = false AND la.attemptTime > :since")
    long countRecentFailedAttemptsByIp(@Param("ipAddress") String ipAddress, @Param("since") LocalDateTime since);

    List<LoginAttempt> findByEmailOrderByAttemptTimeDesc(String email);

    @Modifying
    @Query("DELETE FROM LoginAttempt la WHERE la.attemptTime < :before")
    void deleteOldAttempts(@Param("before") LocalDateTime before);
}
