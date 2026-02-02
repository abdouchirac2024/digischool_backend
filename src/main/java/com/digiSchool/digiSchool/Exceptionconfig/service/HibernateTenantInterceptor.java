package com.digiSchool.digiSchool.Exceptionconfig.service;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class HibernateTenantInterceptor {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {
        Session session = em.unwrap(Session.class);
        session.enableFilter("tenantFilter")
               .setParameter("tenant", TenantContext.getTenant());
    }
}
