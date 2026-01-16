package com.taskflow.backend.web;

//import com.taskflow.domain.Task;
//import com.taskflow.service.TaskService;
import com.taskflow.backend.domain.Task;
import com.taskflow.backend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(request.title(), request.description()));
    }

    // Simple DTO
    public record TaskRequest(String title, String description) {}
}