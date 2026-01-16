package com.taskflow.backend.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.backend.event.TaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    // Use a unique group ID (e.g., appending UUID) if you run multiple instances
    // and want ALL instances to broadcast the socket message.
    // For this POC, we stick to a static group.
    @KafkaListener(topics = "task-updates", groupId = "${spring.kafka.consumer.group-id}")
    public void handleTaskUpdate(String message) {
        try {
            TaskEvent event = objectMapper.readValue(message, TaskEvent.class);
            log.info("Kafka Consumed: Tenant [{}] - Action [{}]", event.getTenantId(), event.getType());

            // Broadcast to WebSocket subscribers
            // Channel: /topic/{tenantId}/tasks
            messagingTemplate.convertAndSend(
                    "/topic/" + event.getTenantId() + "/tasks",
                    event.getTask()
            );

        } catch (Exception e) {
            log.error("Failed to process Kafka message: {}", message, e);
        }
    }
}