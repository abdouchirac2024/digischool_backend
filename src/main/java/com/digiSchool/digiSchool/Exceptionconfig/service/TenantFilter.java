package com.digiSchool.digiSchool.Exceptionconfig.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1) // S'exécute après le filtre CORS de Spring Security
public class TenantFilter extends OncePerRequestFilter {

    @Value("${app.tenant.header}")
    private String tenantHeader;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Skip preflight (OPTIONS) requests - they don't have custom headers
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String tenant = request.getHeader(tenantHeader);

        // Seulement définir le tenant depuis le header si présent
        // Le JwtAuthFilter définira le tenant depuis le token JWT pour les requêtes authentifiées
        if (tenant != null && !tenant.isBlank()) {
            TenantContext.setTenant(tenant);
        }
        // Ne PAS définir de tenant par défaut - laisser le JwtAuthFilter le faire

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}


