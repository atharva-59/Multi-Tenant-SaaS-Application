package com.taskflow.backend.domain;

//import com.taskflow.tenant.TenantContext;
import com.taskflow.backend.tenant.TenantContext;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
// This Filter is the KEY to multi-tenancy. It intercepts queries to add "WHERE tenant_id = ?"
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = String.class)})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public abstract class BaseEntity implements Serializable {

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private String tenantId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Lifecycle hook: Before saving, grab the Tenant ID from the ThreadLocal
    @PrePersist
    public void onPrePersist() {
        this.tenantId = TenantContext.getTenantId();
        if (this.tenantId == null) {
            // Fallback or error logic specifically for non-authenticated contexts (like boot-up)
            // For now, we assume all writes happen in a web context
        }
    }
}