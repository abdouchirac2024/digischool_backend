package com.digiSchool.digiSchool.auth.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.digiSchool.digiSchool.Exceptionconfig.service.TenantContext;
import com.digiSchool.digiSchool.auth.model.User;
import com.digiSchool.digiSchool.auth.repository.UserRepository;
import com.digiSchool.digiSchool.auth.service.JwtTokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtTokenService jwtTokenService, UserRepository userRepository) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String authHeader = request.getHeader("Authorization");

        log.info("[JwtAuthFilter] Path: {} | Auth header: '{}'", path,
            authHeader != null ? authHeader.substring(0, Math.min(20, authHeader.length())) + "..." : "null");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("[JwtAuthFilter] No Bearer token (header doesn't start with 'Bearer '), skipping");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtTokenService.validateToken(token) && jwtTokenService.isAccessToken(token)) {
                String email = jwtTokenService.extractUsername(token);
                String tenantId = jwtTokenService.extractTenantId(token);

                log.info("[JwtAuthFilter] ✓ Token valid | Email: {} | TenantId: {}", email, tenantId);

                // Définir le tenant dans le contexte
                if (tenantId != null) {
                    TenantContext.setTenant(tenantId);
                    log.info("[JwtAuthFilter] ✓ TenantContext set to: {}", tenantId);
                }

                User user = userRepository.findByEmail(email).orElse(null);

                if (user != null && user.isEnabled()) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("[JwtAuthFilter] ✓ User authenticated: {}", email);
                }
            }
        } catch (Exception e) {
            log.error("[JwtAuthFilter] ✗ Token validation failed: {}", e.getMessage());
        }

        // Vérifier TenantContext juste avant de continuer
        log.info("[JwtAuthFilter] TenantContext before filterChain: {}", TenantContext.getTenant());
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/login") ||
               path.startsWith("/api/auth/refresh-token") ||
               path.startsWith("/api/auth/forgot-password") ||
               path.startsWith("/api/auth/reset-password") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs");
    }
}
