package com.example.service;

import com.example.entity.Task;
import com.example.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Task Service - Business logic for task management
 */
@Service
@Slf4j
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Create a new scheduled task
     */
    public Task createTask(String taskName, String description, String frequency) {
        log.info("Creating new task: {} with frequency: {}", taskName, frequency);
        Task task = new Task();
        task.setTaskName(taskName);
        task.setTaskDescription(description);
        task.setExecutionFrequency(frequency);
        task.setStatus(Task.TaskStatus.PENDING);
        task.setScheduledTime(LocalDateTime.now());
        return taskRepository.save(task);
    }

    /**
     * Start a task execution
     */
    public Task startTask(Long taskId) {
        log.info("Starting task ID: {}", taskId);
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            Task t = task.get();
            t.setStatus(Task.TaskStatus.IN_PROGRESS);
            t.setStartedAt(LocalDateTime.now());
            return taskRepository.save(t);
        }
        log.warn("Task not found with ID: {}", taskId);
        return null;
    }

    /**
     * Complete a task
     */
    public Task completeTask(Long taskId) {
        log.info("Completing task ID: {}", taskId);
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            Task t = task.get();
            t.setStatus(Task.TaskStatus.COMPLETED);
            t.setCompletedAt(LocalDateTime.now());
            return taskRepository.save(t);
        }
        return null;
    }

    /**
     * Mark a task as failed with error details
     */
    public Task failTask(Long taskId, String errorDetails) {
        log.warn("Marking task ID: {} as failed with error: {}", taskId, errorDetails);
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            Task t = task.get();
            t.setStatus(Task.TaskStatus.FAILED);
            t.setErrorDetails(errorDetails);
            t.setCompletedAt(LocalDateTime.now());
            return taskRepository.save(t);
        }
        return null;
    }

    /**
     * Get all tasks
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Get task by ID
     */
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Get tasks by status
     */
    public List<Task> getTasksByStatus(Task.TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    /**
     * Get tasks by execution frequency
     */
    public List<Task> getTasksByFrequency(String frequency) {
        return taskRepository.findByExecutionFrequency(frequency);
    }

    /**
     * Get scheduled tasks
     */
    public List<Task> getScheduledTasks(int hours) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
        return taskRepository.findScheduledTasks(startTime);
    }

    /**
     * Get count of tasks by status
     */
    public long getTaskCountByStatus(Task.TaskStatus status) {
        return taskRepository.countByStatus(status);
    }
}
