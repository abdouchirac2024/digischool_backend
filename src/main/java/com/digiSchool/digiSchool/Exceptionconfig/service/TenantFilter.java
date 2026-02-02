package com.digiSchool.digiSchool.Exceptionconfig.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Value("${app.tenant.header}")
    private String tenantHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String tenant = request.getHeader(tenantHeader);

        if (tenant == null) {
            throw new RuntimeException("Tenant manquant"); // en prod
//        	  tenant = "default"; // tenant par défaut en Dev
        }

        TenantContext.setTenant(tenant);
        filterChain.doFilter(request, response);
        TenantContext.clear();
    }
}


