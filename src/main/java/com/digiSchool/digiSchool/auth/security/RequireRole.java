package com.digiSchool.digiSchool.auth.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.digiSchool.digiSchool.auth.model.RoleType;

/**
 * Annotation pour restreindre l'accès à une méthode ou classe selon les rôles.
 * Utilisation: @RequireRole({RoleType.ADMIN_ECOLE, RoleType.SUPER_ADMIN})
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    RoleType[] value();
}
