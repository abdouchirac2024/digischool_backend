package com.digiSchool.digiSchool.auth.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digiSchool.digiSchool.auth.model.RefreshToken;
import com.digiSchool.digiSchool.auth.model.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUserAndRevokedFalse(User user);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user = :user")
    void revokeAllByUser(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.token = :token")
    void revokeByToken(@Param("token") String token);

    @Query("SELECT COUNT(DISTINCT rt.user.id) FROM RefreshToken rt WHERE rt.revoked = false AND rt.expiresAt > :now")
    long countConnectedUsers(@Param("now") LocalDateTime now);

    @Query("SELECT DISTINCT rt.user FROM RefreshToken rt WHERE rt.revoked = false AND rt.expiresAt > :now")
    List<User> findConnectedUsers(@Param("now") LocalDateTime now);

    @Query("SELECT DISTINCT rt.user.id FROM RefreshToken rt WHERE rt.revoked = false AND rt.expiresAt > :now")
    List<Long> findConnectedUserIds(@Param("now") LocalDateTime now);
}
