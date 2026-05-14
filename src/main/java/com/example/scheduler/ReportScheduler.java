package com.example.scheduler;

import com.example.entity.Report;
import com.example.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Report Scheduler - Demonstrates various Spring scheduling concepts
 * 
 * Different scheduling patterns:
 * 1. @Scheduled(fixedRate) - Execute every N milliseconds
 * 2. @Scheduled(fixedDelay) - Execute after N milliseconds from last execution
 * 3. @Scheduled(cron) - Execute using cron expressions
 * 4. @Scheduled(initialDelay) - Initial delay before first execution
 */
@Component
@Slf4j
public class ReportScheduler {

    private final ReportService reportService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ReportScheduler(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * EXAMPLE 1: Fixed Rate Scheduling
     * Executes every 5 minutes (300000 milliseconds)
     * Generates hourly report
     * 
     * Use case: Generate reports at fixed intervals regardless of execution time
     */
    @Scheduled(fixedRate = 300000, initialDelay = 5000)
    public void generateHourlyReport() {
        log.info("\n=== SCHEDULED TASK STARTED: Hourly Report Generation ===");
        try {
            // Create report
            Report report = reportService.createReport("Hourly Report", "SCHEDULER");
            log.info("Report created with ID: {}", report.getId());

            // Start processing
            Report processingReport = reportService.startProcessing(report.getId());
            log.info("Report status: {}", processingReport.getStatus());

            // Simulate report generation
            Thread.sleep(1000);

            // Generate content
            String content = generateReportContent("HOURLY");

            // Complete report
            Report completedReport = reportService.completeReport(report.getId(), content);
            log.info("✓ Hourly report completed successfully at {}", 
                    completedReport.getCompletedAt().format(DATE_FORMATTER));
            log.info("=== SCHEDULED TASK COMPLETED ===");

        } catch (Exception e) {
            log.error("Error generating hourly report: {}", e.getMessage(), e);
        }
    }

    /**
     * EXAMPLE 2: Cron Expression - Daily Report
     * Executes at 2:00 AM every day (cron = "0 0 2 * * *")
     * 
     * Cron format: second minute hour day month day-of-week
     * - 0: at second 0
     * - 0: at minute 0
     * - 2: at hour 2 (2:00 AM)
     * - *: every day of month
     * - *: every month
     * - ?: any day of week
     * 
     * Use case: Generate reports at specific times of day
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void generateDailyReport() {
        log.info("\n=== SCHEDULED TASK STARTED: Daily Report Generation (2:00 AM) ===");
        try {
            Report report = reportService.createReport("Daily Report", "SCHEDULER");
            Report processingReport = reportService.startProcessing(report.getId());
            
            Thread.sleep(1500);
            
            String content = generateReportContent("DAILY");
            Report completedReport = reportService.completeReport(report.getId(), content);
            
            log.info("✓ Daily report generated successfully at {}", 
                    completedReport.getCompletedAt().format(DATE_FORMATTER));
            log.info("=== SCHEDULED TASK COMPLETED ===");

        } catch (Exception e) {
            log.error("Error generating daily report: {}", e.getMessage());
            // Handle error gracefully
        }
    }

    /**
     * EXAMPLE 3: Cron Expression - Weekly Report
     * Executes every Monday at 9:00 AM (cron = "0 0 9 ? * MON")
     * 
     * Cron breakdown:
     * - 0: at second 0
     * - 0: at minute 0
     * - 9: at hour 9 (9:00 AM)
     * - ?: any day of month
     * - *: every month
     * - MON: Monday only
     * 
     * Use case: Generate reports for the previous week
     */
    @Scheduled(cron = "0 0 9 ? * MON")
    public void generateWeeklyReport() {
        log.info("\n=== SCHEDULED TASK STARTED: Weekly Report Generation (Monday 9:00 AM) ===");
        try {
            Report report = reportService.createReport("Weekly Report", "SCHEDULER");
            Report processingReport = reportService.startProcessing(report.getId());
            
            Thread.sleep(2000);
            
            String content = generateReportContent("WEEKLY");
            Report completedReport = reportService.completeReport(report.getId(), content);
            
            log.info("✓ Weekly report generated successfully at {}", 
                    completedReport.getCompletedAt().format(DATE_FORMATTER));
            log.info("=== SCHEDULED TASK COMPLETED ===");

        } catch (Exception e) {
            log.error("Error generating weekly report: {}", e.getMessage());
        }
    }

    /**
     * EXAMPLE 4: Cron Expression - Monthly Report
     * Executes on the 1st of every month at 8:00 AM (cron = "0 0 8 1 * ?")
     * 
     * Cron breakdown:
     * - 0: at second 0
     * - 0: at minute 0
     * - 8: at hour 8 (8:00 AM)
     * - 1: on day 1 of month
     * - *: every month
     * - ?: any day of week
     * 
     * Use case: Generate monthly summary reports
     */
    @Scheduled(cron = "0 0 8 1 * ?")
    public void generateMonthlyReport() {
        log.info("\n=== SCHEDULED TASK STARTED: Monthly Report Generation (1st at 8:00 AM) ===");
        try {
            Report report = reportService.createReport("Monthly Report", "SCHEDULER");
            Report processingReport = reportService.startProcessing(report.getId());
            
            Thread.sleep(3000);
            
            String content = generateReportContent("MONTHLY");
            Report completedReport = reportService.completeReport(report.getId(), content);
            
            log.info("✓ Monthly report generated successfully at {}", 
                    completedReport.getCompletedAt().format(DATE_FORMATTER));
            log.info("=== SCHEDULED TASK COMPLETED ===");

        } catch (Exception e) {
            log.error("Error generating monthly report: {}", e.getMessage());
        }
    }

    /**
     * EXAMPLE 5: Fixed Delay Scheduling
     * Executes with 10 second delay between task completion and next execution
     * initialDelay: 2 seconds before first execution
     * 
     * Use case: Execute tasks sequentially with guaranteed intervals
     * Useful for tasks that should not overlap
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 2000)
    public void generateFlexibleReport() {
        log.info("\n=== SCHEDULED TASK STARTED: Flexible Report Generation ===");
        try {
            Report report = reportService.createReport("Flexible Report", "SCHEDULER");
            Report processingReport = reportService.startProcessing(report.getId());
            
            Thread.sleep(1000);
            
            String content = generateReportContent("FLEXIBLE");
            Report completedReport = reportService.completeReport(report.getId(), content);
            
            log.info("✓ Flexible report generated successfully at {}", 
                    completedReport.getCompletedAt().format(DATE_FORMATTER));
            log.info("=== SCHEDULED TASK COMPLETED ===");

        } catch (Exception e) {
            log.error("Error generating flexible report: {}", e.getMessage());
        }
    }

    /**
     * Helper method to generate report content
     */
    private String generateReportContent(String reportType) {
        LocalDateTime now = LocalDateTime.now();
        return String.format(
            "Report Type: %s\n" +
            "Generated At: %s\n" +
            "Total Reports: %d\n" +
            "Completed Reports: %d\n" +
            "Failed Reports: %d\n" +
            "System Status: HEALTHY",
            reportType,
            now.format(DATE_FORMATTER),
            reportService.getReportCountByStatus(Report.ReportStatus.COMPLETED) +
            reportService.getReportCountByStatus(Report.ReportStatus.FAILED) +
            reportService.getReportCountByStatus(Report.ReportStatus.PENDING),
            reportService.getReportCountByStatus(Report.ReportStatus.COMPLETED),
            reportService.getReportCountByStatus(Report.ReportStatus.FAILED)
        );
    }
}
