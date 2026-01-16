package com.taskflow.backend.repository;

//import com.taskflow.domain.AuditLog;
import com.taskflow.backend.domain.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTenantId(String tenantId);
}