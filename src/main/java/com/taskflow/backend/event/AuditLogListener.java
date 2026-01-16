package com.taskflow.backend.event;

import com.taskflow.backend.domain.AuditLog;
import com.taskflow.backend.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogListener {

    private final AuditLogRepository auditLogRepository;

    @Async
    @EventListener
    // REQUIRES_NEW ensures if main tx fails, audit might still save (or vice versa depending on requirement)
    // But usually for audit, we just want it to happen in background.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleAuditLogEvent(AuditLogEvent event) {
        log.info("Async logging for tenant: {}", event.getTenantId());

        AuditLog logEntry = AuditLog.builder()
                .tenantId(event.getTenantId())
                .userId(event.getUserId())
                .action(event.getAction())
                .details(event.getDetails())
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(logEntry);
    }
}