package com.digiSchool.digiSchool.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.digiSchool.digiSchool.auth.filter.JwtAuthenticationFilter;

/**
 * Configuration de la sécurité Spring Security.
 *
 * Caractéristiques:
 * - Authentification stateless via JWT
 * - CORS configuré pour le développement local
 * - CSRF désactivé (API REST)
 * - Endpoints publics: /api/auth/**, /swagger-ui/**, /actuator/**
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Configuration CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Désactiver CSRF (non nécessaire pour API REST avec JWT)
            .csrf(csrf -> csrf.disable())

            // Désactiver les authentifications par défaut de Spring Security
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())

            // Gestion des sessions stateless (JWT)
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Configuration des autorisations
            .authorizeHttpRequests(auth -> auth
                // Endpoints publics (pas besoin d'authentification)
                .requestMatchers(
                    "/api/auth/login",        // Login
                    "/api/auth/refresh-token", // Rafraîchir token
                    "/api/auth/forgot-password", // Mot de passe oublié
                    "/api/auth/reset-password",  // Réinitialisation mot de passe
                    "/api/public/**",         // Endpoints publics
                    "/swagger-ui/**",         // Documentation Swagger
                    "/swagger-ui.html",
                    "/v3/api-docs/**",        // OpenAPI docs
                    "/actuator/health",       // Health check uniquement
                    "/error",                 // Page d'erreur Spring
                    "/api/files/*/info",      // Infos fichier (lecture publique)
                    "/api/files/*"            // Téléchargement fichiers MongoDB GridFS (IDs ObjectId = non-devinables)
                ).permitAll()

                // Tous les autres endpoints /api/** nécessitent une authentification
                .requestMatchers("/api/**").authenticated()

                // Tout le reste nécessite authentification
                .anyRequest().authenticated()
            )

            // Ajouter le filtre JWT avant le filtre d'authentification standard
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuration CORS pour autoriser les requêtes cross-origin.
     * SÉCURITÉ: Origines spécifiques uniquement (pas de wildcards avec credentials)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // SÉCURITÉ: Origines explicites uniquement
        // En production, définir via variable d'environnement CORS_ALLOWED_ORIGINS
        String allowedOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        } else {
            // Développement + Vercel
            configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "https://*.vercel.app",
                "https://*.onrender.com",
                "https://helpdigischool.com",
                "https://*.helpdigischool.com"
            ));
        }

        // Méthodes HTTP autorisées
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"
        ));

        // Headers autorisés dans les requêtes
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));

        // Headers exposés dans les réponses
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "X-Total-Count",
            "X-Page-Number",
            "X-Page-Size"
        ));

        // Autoriser les credentials (cookies, Authorization header)
        configuration.setAllowCredentials(true);

        // Durée de mise en cache des résultats preflight (1 heure)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Encodeur de mots de passe BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}