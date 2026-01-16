package com.taskflow.backend.tenant;

import com.taskflow.backend.tenant.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TenantFilterAspect extends OncePerRequestFilter {

    private final EntityManager entityManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Grab the session from the EntityManager
        Session session = entityManager.unwrap(Session.class);

        String tenantId = TenantContext.getTenantId();

        // If we have a tenant (extracted from JWT in a previous filter), enable the data filter
        if (tenantId != null) {
            session.enableFilter("tenantFilter").setParameter("tenantId", tenantId);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clean up ensures we don't leak filters into other requests in connection pooling
            session.disableFilter("tenantFilter");
        }
    }
}