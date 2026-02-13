package com.digiSchool.digiSchool.auth.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.auth.service.JwtTokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtre d'authentification JWT.
 * Intercepte chaque requête pour valider le token JWT et établir le contexte de
 * sécurité.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtService;

    public JwtAuthenticationFilter(JwtTokenService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Récupérer le header Authorization
        String authHeader = request.getHeader("Authorization");

        // Si pas de token, continuer sans authentification
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraire le token (sans "Bearer ")
        String token = authHeader.substring(7);

        try {
            // Valider le token
            if (jwtService.validateToken(token)) {
                String username = jwtService.extractUsername(token);
                String role = jwtService.extractRole(token);

                // Créer les autorités basées sur le rôle
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                }

                // Créer l'objet d'authentification
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
                        null, authorities);

                // Ajouter les détails de la requête
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Établir le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Définir le TenantContext (Data Isolation)
                String ecoleId = jwtService.extractTenantId(token);
                if (ecoleId != null) {
                    TenantContext.setTenant(ecoleId);
                }
            }
        } catch (Exception e) {
            // Token invalide - continuer sans authentification
            // Le contrôleur gérera le cas non authentifié si nécessaire
            SecurityContextHolder.clearContext();
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    /**
     * Ne pas filtrer les endpoints publics pour optimiser les performances.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Liste des endpoints publics qui n'ont pas besoin de vérification JWT
        return path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/actuator/health");
    }
}