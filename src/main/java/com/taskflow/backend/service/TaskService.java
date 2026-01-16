package com.taskflow.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.backend.domain.Task;
import com.taskflow.backend.domain.TaskStatus;
//import com.taskflow.backend.dto.TaskEvent;
import com.taskflow.backend.event.TaskEvent;
import com.taskflow.backend.repository.TaskRepository;
import com.taskflow.backend.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, String> kafkaTemplate; // Inject Kafka
    private final ObjectMapper objectMapper;

    // 1. REDIS CACHING
    @Cacheable(value = "tasks", key = "T(com.taskflow.backend.tenant.TenantContext).getTenantId()")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Transactional
    // 2. CACHE EVICTION
    @CacheEvict(value = "tasks", key = "T(com.taskflow.backend.tenant.TenantContext).getTenantId()")
    public Task createTask(String title, String description) {

        // A. Save to DB
        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(TaskStatus.TODO)
                .build();
        Task savedTask = taskRepository.save(task);

        String currentTenant = TenantContext.getTenantId();

        // B. Publish to Kafka (Async Broadcast)
        try {
            TaskEvent event = new TaskEvent(currentTenant, "CREATED", savedTask);
            String jsonMessage = objectMapper.writeValueAsString(event);

            // Send to topic "task-updates"
            kafkaTemplate.send("task-updates", jsonMessage);

        } catch (Exception e) {
            // Log error, but don't fail the transaction just because notification failed
            e.printStackTrace();
        }

        return savedTask;
    }
}