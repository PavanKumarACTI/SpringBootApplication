package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Task Entity - Represents a scheduled task execution
 */
@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String taskName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING;

    @Column(length = 500)
    private String taskDescription;

    @Column(nullable = false, updatable = false)
    private LocalDateTime scheduledTime = LocalDateTime.now();

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @Column(length = 500)
    private String errorDetails;

    @Column(nullable = false)
    private String executionFrequency; // e.g., "HOURLY", "DAILY", "WEEKLY", "MONTHLY"

    /**
     * Task Status Enum
     */
    public enum TaskStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        SKIPPED
    }
}
