package com.taskflow.backend.event;

import com.taskflow.backend.domain.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskEvent implements Serializable {
    private String tenantId;
    private String type; // "CREATED", "UPDATED"
    private Task task;
}