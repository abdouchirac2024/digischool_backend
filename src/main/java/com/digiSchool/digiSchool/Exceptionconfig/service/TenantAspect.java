package com.digiSchool.digiSchool.Exceptionconfig.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Aspect qui active le filtre Hibernate tenant pour chaque appel de service.
 * Cela garantit que les requêtes de base de données ne renvoient que les données
 * du tenant de l'utilisateur authentifié.
 */
@Aspect
@Component
public class TenantAspect {

    private static final Logger log = LoggerFactory.getLogger(TenantAspect.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Active le filtre tenant avant chaque méthode de service qui accède aux données.
     * Couvre tous les packages de services du projet.
     */
    @Around("(execution(* com.digiSchool.digiSchool.user.serviceimp..*(..)) || " +
            "execution(* com.digiSchool.digiSchool.user.service..*(..)) || " +
            "execution(* com.digiSchool.digiSchool.academic..service..*(..)) || " +
            "execution(* com.digiSchool.digiSchool.academic..serviceimp..*(..)) || " +
            "within(@org.springframework.stereotype.Service *)) && " +
            "!within(com.digiSchool.digiSchool.auth.service.AuthService) && " +
            "!within(com.digiSchool.digiSchool.auth.service.JwtTokenService) && " +
            "!within(com.digiSchool.digiSchool.auth.service.CustomUserDetailsService) && " +
            "!within(com.digiSchool.digiSchool.academic.organisation.serviceimp.EcoleServiceImpl)")
    public Object enableTenantFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        String tenant = TenantContext.getTenant();
        String methodName = joinPoint.getSignature().toShortString();

        log.info("[TenantAspect] Method: {} | Tenant from context: {}", methodName, tenant);

        if (tenant != null && !tenant.isBlank()) {
            try {
                Session session = entityManager.unwrap(Session.class);
                session.enableFilter("tenantFilter")
                       .setParameter("tenant", tenant);
                log.info("[TenantAspect] ✓ Filter enabled for tenant: {}", tenant);
            } catch (Exception e) {
                log.error("[TenantAspect] ✗ Failed to enable filter: {}", e.getMessage());
            }
        } else {
            // Pas de tenant : endpoint public (ex: inscription école) - procéder sans filtre
            // La sécurité est gérée par Spring Security (@RequireRole) pour les endpoints protégés
            log.warn("[TenantAspect] ⚠ No tenant in context for method: {} - proceeding without filter (public endpoint)", methodName);
        }

        return joinPoint.proceed();
    }
}
