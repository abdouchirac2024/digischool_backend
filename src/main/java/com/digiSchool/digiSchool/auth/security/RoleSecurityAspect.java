package com.digiSchool.digiSchool.auth.security;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.digiSchool.digiSchool.auth.model.RoleType;
import com.digiSchool.digiSchool.auth.model.User;

@Aspect
@Component
public class RoleSecurityAspect {

    @Around("@annotation(requireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Authentification requise");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            throw new AccessDeniedException("Authentification invalide");
        }

        User user = (User) principal;
        RoleType[] allowedRoles = requireRole.value();

        boolean hasRole = Arrays.stream(allowedRoles)
                .anyMatch(role -> role == user.getRole());

        if (!hasRole) {
            throw new AccessDeniedException("Accès refusé. Rôles requis: " + Arrays.toString(allowedRoles));
        }

        return joinPoint.proceed();
    }
}
