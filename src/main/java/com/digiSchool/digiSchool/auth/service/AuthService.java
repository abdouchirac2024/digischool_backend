package com.digiSchool.digiSchool.auth.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digiSchool.digiSchool.auth.dto.AuthResponse;
import com.digiSchool.digiSchool.auth.dto.ForgotPasswordRequest;
import com.digiSchool.digiSchool.auth.dto.LoginRequest;
import com.digiSchool.digiSchool.auth.dto.RefreshTokenRequest;
import com.digiSchool.digiSchool.auth.dto.ResetPasswordRequest;
import com.digiSchool.digiSchool.auth.exception.AuthenticationException;
import com.digiSchool.digiSchool.auth.model.LoginAttempt;
import com.digiSchool.digiSchool.auth.model.RefreshToken;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.model.UserStatus;
import com.digiSchool.digiSchool.auth.repository.LoginAttemptRepository;
import com.digiSchool.digiSchool.auth.repository.RefreshTokenRepository;
import com.digiSchool.digiSchool.auth.repository.UserRepository;
import com.digiSchool.digiSchool.notification.service.EmailService;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${auth.max-login-attempts:5}")
    private int maxLoginAttempts;

    @Value("${auth.lockout-duration-minutes:15}")
    private int lockoutDurationMinutes;

    @Value("${auth.rate-limit-window-minutes:5}")
    private int rateLimitWindowMinutes;

    @Value("${jwt.refresh-token-expiration:604800000}")
    private Long refreshTokenExpiration;

    public AuthService(UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            LoginAttemptRepository loginAttemptRepository,
            JwtTokenService jwtTokenService,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public AuthResponse login(LoginRequest request, String ipAddress) {
        String login = request.getLogin().trim();

        // Vérifier le rate limiting
        checkRateLimit(login, ipAddress);

        // Détecter si c'est un email (contient @) ou un téléphone
        boolean isEmail = login.contains("@");

        // Trouver l'utilisateur
        User user;
        if (isEmail) {
            user = userRepository.findByEmail(login.toLowerCase())
                    .orElseThrow(() -> {
                        logFailedAttempt(login, ipAddress, "Utilisateur non trouvé");
                        return new AuthenticationException("Identifiant ou mot de passe incorrect");
                    });
        } else {
            user = userRepository.findByTelephone(login)
                    .orElseThrow(() -> {
                        logFailedAttempt(login, ipAddress, "Utilisateur non trouvé");
                        return new AuthenticationException("Identifiant ou mot de passe incorrect");
                    });
        }

        // Vérifier si le compte est verrouillé
        if (user.getStatus() == UserStatus.LOCKED) {
            if (user.getLockedUntil() != null && LocalDateTime.now().isAfter(user.getLockedUntil())) {
                // Déverrouiller le compte si la période de verrouillage est terminée
                user.setStatus(UserStatus.ACTIVE);
                user.setFailedLoginAttempts(0);
                user.setLockedUntil(null);
                userRepository.save(user);
            } else {
                logFailedAttempt(login, ipAddress, "Compte verrouillé");
                throw new AuthenticationException("Compte verrouillé. Réessayez plus tard.");
            }
        }

        // Vérifier si le compte est actif
        if (user.getStatus() != UserStatus.ACTIVE) {
            logFailedAttempt(login, ipAddress, "Compte inactif");
            throw new AuthenticationException("Compte inactif. Contactez l'administrateur.");
        }

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            handleFailedLogin(user, ipAddress);
            throw new AuthenticationException("Identifiant ou mot de passe incorrect");
        }

        // Connexion réussie
        return handleSuccessfulLogin(user, ipAddress);
    }

    private void checkRateLimit(String email, String ipAddress) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(rateLimitWindowMinutes);

        long failedAttemptsByEmail = loginAttemptRepository.countRecentFailedAttempts(email, since);
        long failedAttemptsByIp = loginAttemptRepository.countRecentFailedAttemptsByIp(ipAddress, since);

        if (failedAttemptsByEmail >= maxLoginAttempts || failedAttemptsByIp >= maxLoginAttempts * 2) {
            throw new AuthenticationException(
                    "Trop de tentatives de connexion. Réessayez dans " + rateLimitWindowMinutes + " minutes.");
        }
    }

    private void handleFailedLogin(User user, String ipAddress) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= maxLoginAttempts) {
            user.setStatus(UserStatus.LOCKED);
            user.setLockedUntil(LocalDateTime.now().plusMinutes(lockoutDurationMinutes));
            logFailedAttempt(user.getEmail(), ipAddress, "Compte verrouillé après " + attempts + " tentatives");
        } else {
            logFailedAttempt(user.getEmail(), ipAddress, "Mot de passe incorrect (tentative " + attempts + ")");
        }

        userRepository.save(user);
    }

    private AuthResponse handleSuccessfulLogin(User user, String ipAddress) {
        // Réinitialiser les tentatives échouées
        user.setFailedLoginAttempts(0);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Logger la tentative réussie
        logSuccessfulAttempt(user.getEmail(), ipAddress);

        // Générer les tokens
        String accessToken = jwtTokenService.generateAccessToken(user);
        String refreshToken = jwtTokenService.generateRefreshToken(user);

        // Sauvegarder le refresh token
        RefreshToken refreshTokenEntity = new RefreshToken(
                refreshToken,
                user,
                LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000));
        refreshTokenRepository.save(refreshTokenEntity);

        // Construire la réponse
        AuthResponse.UserInfo userInfo = buildUserInfo(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtTokenService.getAccessTokenExpiration(),
                userInfo);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String token = request.getRefreshToken();

        // Vérifier si le token est valide
        if (!jwtTokenService.validateToken(token) || !jwtTokenService.isRefreshToken(token)) {
            throw new AuthenticationException("Refresh token invalide");
        }

        // Vérifier si le token existe en base et n'est pas révoqué
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new AuthenticationException("Refresh token non trouvé"));

        if (!refreshTokenEntity.isValid()) {
            throw new AuthenticationException("Refresh token expiré ou révoqué");
        }

        User user = refreshTokenEntity.getUser();

        // Vérifier si l'utilisateur est toujours actif
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AuthenticationException("Compte utilisateur inactif");
        }

        // Révoquer l'ancien refresh token
        refreshTokenEntity.setRevoked(true);
        refreshTokenRepository.save(refreshTokenEntity);

        // Générer de nouveaux tokens
        String newAccessToken = jwtTokenService.generateAccessToken(user);
        String newRefreshToken = jwtTokenService.generateRefreshToken(user);

        // Sauvegarder le nouveau refresh token
        RefreshToken newRefreshTokenEntity = new RefreshToken(
                newRefreshToken,
                user,
                LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000));
        refreshTokenRepository.save(newRefreshTokenEntity);

        AuthResponse.UserInfo userInfo = buildUserInfo(user);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                jwtTokenService.getAccessTokenExpiration(),
                userInfo);
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    public void logoutAll(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("Utilisateur non trouvé"));
        refreshTokenRepository.revokeAllByUser(user);
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        String email = request.getEmail().toLowerCase().trim();

        userRepository.findByEmail(email).ifPresent(user -> {
            String resetToken = UUID.randomUUID().toString();
            user.setPasswordResetToken(resetToken);
            user.setPasswordResetExpires(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            emailService.sendPasswordResetEmail(email, resetToken);
        });

        // Ne pas révéler si l'email existe ou non (sécurité)
    }

    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByPasswordResetToken(request.getToken())
                .orElseThrow(() -> new AuthenticationException("Token de réinitialisation invalide"));

        if (user.getPasswordResetExpires() == null ||
                LocalDateTime.now().isAfter(user.getPasswordResetExpires())) {
            throw new AuthenticationException("Token de réinitialisation expiré");
        }

        // Mettre à jour le mot de passe
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpires(null);
        user.setFailedLoginAttempts(0);

        if (user.getStatus() == UserStatus.LOCKED) {
            user.setStatus(UserStatus.ACTIVE);
            user.setLockedUntil(null);
        }

        userRepository.save(user);

        // Révoquer tous les refresh tokens existants
        refreshTokenRepository.revokeAllByUser(user);
    }

    public User getCurrentUser(String token) {
        if (!jwtTokenService.validateToken(token)) {
            throw new AuthenticationException("Token invalide");
        }

        Long userId = jwtTokenService.extractUserId(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("Utilisateur non trouvé"));
    }

    private void logFailedAttempt(String email, String ipAddress, String reason) {
        LoginAttempt attempt = new LoginAttempt(email, ipAddress, false, reason);
        loginAttemptRepository.save(attempt);
    }

    private void logSuccessfulAttempt(String email, String ipAddress) {
        LoginAttempt attempt = new LoginAttempt(email, ipAddress, true, null);
        loginAttemptRepository.save(attempt);
    }

    public AuthResponse.UserInfo buildUserInfo(User user) {
        return new AuthResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                user.getTelephone(),
                user.getRole(),
                user.getTenantId(),
                user.getEcole() != null ? user.getEcole().getIdEcole() : null,
                user.getEcole() != null ? user.getEcole().getNom() : null,
                user.getEcole() != null ? user.getEcole().getCodeEcole() : null);
    }
}
