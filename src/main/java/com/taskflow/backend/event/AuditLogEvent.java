package com.taskflow.backend.event;

import com.taskflow.backend.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;

@Getter
public class AuditLogEvent extends ApplicationEvent {
    private final String tenantId;
    private final String userId;
    private final String action;
    private final String details;

    public AuditLogEvent(Object source, String tenantId, String userId, String action, String details) {
        super(source);
        this.tenantId = tenantId;
        this.userId = userId;
        this.action = action;
        this.details = details;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TaskEvent implements Serializable {
        private String tenantId;
        private String type; // "CREATED", "UPDATED"
        private Task task;
    }
}