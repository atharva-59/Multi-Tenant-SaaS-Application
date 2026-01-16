package com.taskflow.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog { // Note: No BaseEntity, we want full control

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tenantId;
    private String userId;     // Who did it?
    private String action;     // CREATE, UPDATE, DELETE
    private String details;    // "Created task ID 5"

    private LocalDateTime timestamp;
}