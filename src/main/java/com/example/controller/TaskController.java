package com.example.controller;

import com.example.entity.Task;
import com.example.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Task Controller - REST endpoints for task management
 */
@RestController
@RequestMapping("/api/tasks")
@Slf4j
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * GET /api/tasks - Get all tasks
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("Fetching all tasks");
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * GET /api/tasks/{id} - Get task by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        log.info("Fetching task with ID: {}", id);
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET /api/tasks/status/{status} - Get tasks by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        log.info("Fetching tasks with status: {}", status);
        try {
            Task.TaskStatus taskStatus = Task.TaskStatus.valueOf(status.toUpperCase());
            List<Task> tasks = taskService.getTasksByStatus(taskStatus);
            return ResponseEntity.ok(tasks);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status: {}", status);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/tasks/frequency/{frequency} - Get tasks by execution frequency
     */
    @GetMapping("/frequency/{frequency}")
    public ResponseEntity<List<Task>> getTasksByFrequency(@PathVariable String frequency) {
        log.info("Fetching tasks with frequency: {}", frequency);
        List<Task> tasks = taskService.getTasksByFrequency(frequency.toUpperCase());
        return ResponseEntity.ok(tasks);
    }

    /**
     * POST /api/tasks - Create a new task
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Map<String, String> request) {
        log.info("Creating new task with name: {}", request.get("taskName"));
        String taskName = request.get("taskName");
        String description = request.getOrDefault("taskDescription", "");
        String frequency = request.getOrDefault("executionFrequency", "HOURLY");
        
        if (taskName == null || taskName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        
        Task task = taskService.createTask(taskName, description, frequency);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    /**
     * PUT /api/tasks/{id}/start - Start a task
     */
    @PutMapping("/{id}/start")
    public ResponseEntity<Task> startTask(@PathVariable Long id) {
        log.info("Starting task with ID: {}", id);
        Task task = taskService.startTask(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    /**
     * PUT /api/tasks/{id}/complete - Complete a task
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long id) {
        log.info("Completing task with ID: {}", id);
        Task task = taskService.completeTask(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    /**
     * PUT /api/tasks/{id}/fail - Mark task as failed
     */
    @PutMapping("/{id}/fail")
    public ResponseEntity<Task> failTask(@PathVariable Long id, @RequestBody Map<String, String> request) {
        log.info("Failing task with ID: {}", id);
        String errorDetails = request.getOrDefault("errorDetails", "Unknown error");
        Task task = taskService.failTask(id, errorDetails);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    /**
     * GET /api/tasks/stats/count - Get task statistics
     */
    @GetMapping("/stats/count")
    public ResponseEntity<Map<String, Long>> getTaskStats() {
        log.info("Fetching task statistics");
        Map<String, Long> stats = Map.of(
            "PENDING", taskService.getTaskCountByStatus(Task.TaskStatus.PENDING),
            "IN_PROGRESS", taskService.getTaskCountByStatus(Task.TaskStatus.IN_PROGRESS),
            "COMPLETED", taskService.getTaskCountByStatus(Task.TaskStatus.COMPLETED),
            "FAILED", taskService.getTaskCountByStatus(Task.TaskStatus.FAILED)
        );
        return ResponseEntity.ok(stats);
    }
}
