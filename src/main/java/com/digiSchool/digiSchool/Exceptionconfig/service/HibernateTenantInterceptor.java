package com.digiSchool.digiSchool.Exceptionconfig.service;

/**
 * @deprecated Cette classe est obsolète. Le filtrage par tenant est maintenant géré par TenantAspect.
 * Le filtre Hibernate est activé dynamiquement par requête via l'aspect.
 */
// @Component - Désactivé, remplacé par TenantAspect
public class HibernateTenantInterceptor {
    // Cette classe n'est plus utilisée
    // Le filtrage multi-tenant est géré par TenantAspect qui active le filtre Hibernate
    // dynamiquement à chaque appel de service avec le tenant du contexte JWT
}
