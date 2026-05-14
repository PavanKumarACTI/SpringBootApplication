package com.example.controller;

import com.example.entity.Report;
import com.example.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Report Controller
 * 
 * REST API endpoints for report management
 * 
 * Base URL: http://localhost:8080/api/reports
 */
@Slf4j
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    /**
     * Get all reports
     * GET /api/reports
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllReports() {
        log.info("Fetching all reports");
        
        List<Report> reports = reportService.getAllReports();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", reports);
        response.put("count", reports.size());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get report by ID
     * GET /api/reports/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getReportById(@PathVariable Long id) {
        log.info("Fetching report with ID: {}", id);
        
        Optional<Report> report = reportService.getReportById(id);
        Map<String, Object> response = new HashMap<>();
        
        if (report.isPresent()) {
            response.put("status", "success");
            response.put("data", report.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Report not found with ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    /**
     * Get reports by status
     * GET /api/reports/status/{status}
     * Status values: PENDING, IN_PROGRESS, COMPLETED, FAILED
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Map<String, Object>> getReportsByStatus(@PathVariable String status) {
        log.info("Fetching reports with status: {}", status);
        
        try {
            Report.ReportStatus reportStatus = Report.ReportStatus.valueOf(status.toUpperCase());
            List<Report> reports = reportService.getReportsByStatus(reportStatus);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", reports);
            response.put("count", reports.size());
            response.put("filter", status);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Invalid status: " + status);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get reports from last N hours
     * GET /api/reports/recent/{hours}
     */
    @GetMapping("/recent/{hours}")
    public ResponseEntity<Map<String, Object>> getRecentReports(@PathVariable int hours) {
        log.info("Fetching reports from last {} hours", hours);
        
        List<Report> reports = reportService.getRecentReports(hours);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", reports);
        response.put("count", reports.size());
        response.put("timeRange", hours + " hours");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Create a new report (manual trigger)
     * POST /api/reports
     * 
     * Request body:
     * {
     *   "reportName": "Manual Report",
     *   "content": "Report content here"
     * }
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createReport(@RequestBody Map<String, String> request) {
        log.info("Creating new report: {}", request.get("reportName"));
        
        try {
            String reportName = request.getOrDefault("reportName", "Manual Report");
            String content = request.getOrDefault("content", "");
            
            Report report = reportService.createReport(reportName, content);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Report created successfully");
            response.put("data", report);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Update report status
     * PUT /api/reports/{id}/status
     * 
     * Request body:
     * {
     *   "status": "COMPLETED"
     * }
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateReportStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        
        log.info("Updating report status for ID: {}", id);
        
        try {
            String statusStr = request.get("status");
            Report.ReportStatus status = Report.ReportStatus.valueOf(statusStr.toUpperCase());
            
            Report report = reportService.updateReportStatus(id, status);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Report status updated successfully");
            response.put("data", report);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Invalid status value");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Delete report
     * DELETE /api/reports/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteReport(@PathVariable Long id) {
        log.info("Deleting report with ID: {}", id);
        
        try {
            reportService.deleteReport(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Report deleted successfully");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Health check endpoint
     * GET /api/reports/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Report API");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
