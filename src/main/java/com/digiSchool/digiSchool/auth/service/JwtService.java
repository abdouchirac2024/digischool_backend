package com.digiSchool.digiSchool.auth.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

/**
 * Service de gestion des tokens JWT.
 * Gère la génération, validation et extraction des informations des tokens.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration; // 1 jour par défaut

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpiration; // 7 jours par défaut

    /**
     * Génère un token JWT avec les informations de l'utilisateur.
     *
     * @param username Email de l'utilisateur
     * @param ecoleId  ID de l'école (tenant)
     * @param roleName Nom du rôle
     * @return Token JWT signé
     */
    public String generateToken(String username, Long ecoleId, String roleName) {
        Map<String, Object> claims = new HashMap<>();
        if (ecoleId != null) {
            claims.put("ecoleId", ecoleId);
        }
        if (roleName != null) {
            claims.put("role", roleName);
        }

        return buildToken(claims, username, jwtExpiration);
    }

    /**
     * Génère un refresh token.
     *
     * @param username Email de l'utilisateur
     * @return Refresh token JWT
     */
    public String generateRefreshToken(String username) {
        return buildToken(new HashMap<>(), username, refreshExpiration);
    }

    /**
     * Génère un token simple (rétrocompatibilité).
     */
    public String generateToken(String username) {
        return generateToken(username, null, null);
    }

    /**
     * Construit le token JWT.
     */
    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrait le nom d'utilisateur (email) du token.
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrait l'ID de l'école depuis le token JWT.
     */
    public Long extractEcoleId(String token) {
        Claims claims = getClaims(token);
        Object ecoleId = claims.get("ecoleId");
        if (ecoleId == null) return null;
        if (ecoleId instanceof Integer) {
            return ((Integer) ecoleId).longValue();
        }
        return (Long) ecoleId;
    }

    /**
     * Extrait le rôle depuis le token JWT.
     */
    public String extractRole(String token) {
        return (String) getClaims(token).get("role");
    }

    /**
     * Extrait la date d'expiration du token.
     */
    public Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    /**
     * Vérifie si le token est valide et non expiré.
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Vérifie si le token est valide pour un utilisateur spécifique.
     */
    public boolean isTokenValid(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            return tokenUsername.equals(username) && isTokenValid(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parse et valide le token pour extraire les claims.
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retourne le temps d'expiration du token en millisecondes.
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }
}