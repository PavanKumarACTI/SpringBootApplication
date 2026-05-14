package com.example.repository;

import com.example.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Task Repository - Data access layer for Task entity
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find all tasks by status
     */
    List<Task> findByStatus(Task.TaskStatus status);

    /**
     * Find tasks by execution frequency
     */
    List<Task> findByExecutionFrequency(String frequency);

    /**
     * Find tasks scheduled after a specific time
     */
    @Query("SELECT t FROM Task t WHERE t.scheduledTime >= :startTime ORDER BY t.scheduledTime DESC")
    List<Task> findScheduledTasks(@Param("startTime") LocalDateTime startTime);

    /**
     * Count tasks by status
     */
    long countByStatus(Task.TaskStatus status);

    /**
     * Find failed tasks
     */
    List<Task> findByStatusAndErrorDetailsIsNotNull(Task.TaskStatus status);
}
