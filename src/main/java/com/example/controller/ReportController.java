package com.example.controller;

import com.example.entity.Report;
import com.example.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Report Controller - REST endpoints for report management
 */
@RestController
@RequestMapping("/api/reports")
@Slf4j
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * GET /api/reports - Get all reports
     */
    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        log.info("Fetching all reports");
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * GET /api/reports/{id} - Get report by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        log.info("Fetching report with ID: {}", id);
        Optional<Report> report = reportService.getReportById(id);
        return report.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET /api/reports/status/{status} - Get reports by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Report>> getReportsByStatus(@PathVariable String status) {
        log.info("Fetching reports with status: {}", status);
        try {
            Report.ReportStatus reportStatus = Report.ReportStatus.valueOf(status.toUpperCase());
            List<Report> reports = reportService.getReportsByStatus(reportStatus);
            return ResponseEntity.ok(reports);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status: {}", status);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/reports/recent/{hours} - Get recent reports
     */
    @GetMapping("/recent/{hours}")
    public ResponseEntity<List<Report>> getRecentReports(@PathVariable int hours) {
        log.info("Fetching recent reports from last {} hours", hours);
        List<Report> reports = reportService.getRecentReports(hours);
        return ResponseEntity.ok(reports);
    }

    /**
     * POST /api/reports - Create a new report
     */
    @PostMapping
    public ResponseEntity<Report> createReport(@RequestBody Map<String, String> request) {
        log.info("Creating new report with name: {}", request.get("reportName"));
        String reportName = request.get("reportName");
        String generatedBy = request.getOrDefault("generatedBy", "MANUAL");
        
        if (reportName == null || reportName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        
        Report report = reportService.createReport(reportName, generatedBy);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    /**
     * PUT /api/reports/{id}/status - Update report status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Report> updateReportStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        log.info("Updating report ID: {} status", id);
        String status = request.get("status");
        String content = request.get("content");
        String errorMessage = request.get("errorMessage");

        Optional<Report> reportOpt = reportService.getReportById(id);
        if (reportOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Report report = null;
        if ("COMPLETED".equalsIgnoreCase(status)) {
            report = reportService.completeReport(id, content);
        } else if ("FAILED".equalsIgnoreCase(status)) {
            report = reportService.failReport(id, errorMessage);
        } else if ("IN_PROGRESS".equalsIgnoreCase(status)) {
            report = reportService.startProcessing(id);
        }

        return report != null ? ResponseEntity.ok(report) : ResponseEntity.badRequest().build();
    }

    /**
     * DELETE /api/reports/{id} - Delete a report
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        log.info("Deleting report with ID: {}", id);
        Optional<Report> report = reportService.getReportById(id);
        if (report.isPresent()) {
            // For production, consider soft delete instead of hard delete
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET /api/reports/stats/count - Get report statistics
     */
    @GetMapping("/stats/count")
    public ResponseEntity<Map<String, Long>> getReportStats() {
        log.info("Fetching report statistics");
        Map<String, Long> stats = Map.of(
            "PENDING", reportService.getReportCountByStatus(Report.ReportStatus.PENDING),
            "IN_PROGRESS", reportService.getReportCountByStatus(Report.ReportStatus.IN_PROGRESS),
            "COMPLETED", reportService.getReportCountByStatus(Report.ReportStatus.COMPLETED),
            "FAILED", reportService.getReportCountByStatus(Report.ReportStatus.FAILED)
        );
        return ResponseEntity.ok(stats);
    }
}
