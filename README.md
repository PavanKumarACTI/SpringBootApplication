# Spring Boot Scheduling Application

A comprehensive Spring Boot application demonstrating various scheduling concepts for automated report generation and task management.

## 📋 Overview

This application showcases how to implement scheduled tasks in Spring Boot using:
- **@Scheduled annotation** with fixed rates and delays
- **Cron expressions** for time-based scheduling
- **JPA repositories** for database persistence
- **REST APIs** for report and task management

## 🏗️ Project Structure

```
src/main/
├── java/com/example/
│   ├── SpringBootSchedulingApplication.java    # Main application class
│   ├── entity/
│   │   ├── Report.java                         # Report entity
│   │   └── Task.java                           # Task entity
│   ├── repository/
│   │   ├── ReportRepository.java                # Report data access
│   │   └── TaskRepository.java                 # Task data access
│   ├── service/
│   │   ├── ReportService.java                  # Report business logic
│   │   └── TaskService.java                    # Task business logic
│   ├── scheduler/
│   │   └── ReportScheduler.java                # Scheduled tasks
│   └── controller/
│       ├── ReportController.java                # Report REST endpoints
│       └── TaskController.java                 # Task REST endpoints
└── resources/
    └── application.properties                   # Application configuration
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd SpringBootApplication
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 📅 Scheduling Examples

The `ReportScheduler` class contains 5 examples of Spring scheduling:

### 1. Fixed Rate Scheduling (Every 5 minutes)
```java
@Scheduled(fixedRate = 300000, initialDelay = 5000)
public void generateHourlyReport() { ... }
```
**Use Case**: Generate reports at consistent intervals

### 2. Daily Report (2:00 AM)
```java
@Scheduled(cron = "0 0 2 * * ?")
public void generateDailyReport() { ... }
```
**Cron Format**: `second minute hour day month day-of-week`

### 3. Weekly Report (Monday 9:00 AM)
```java
@Scheduled(cron = "0 0 9 ? * MON")
public void generateWeeklyReport() { ... }
```
**Use Case**: Generate weekly summaries on specific days

### 4. Monthly Report (1st at 8:00 AM)
```java
@Scheduled(cron = "0 0 8 1 * ?")
public void generateMonthlyReport() { ... }
```
**Use Case**: Generate month-end reports

### 5. Fixed Delay Scheduling (10 second delay)
```java
@Scheduled(fixedDelay = 10000, initialDelay = 2000)
public void generateFlexibleReport() { ... }
```
**Use Case**: Execute tasks sequentially with guaranteed intervals

## 📡 REST API Endpoints

### Report Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/reports` | Get all reports |
| GET | `/api/reports/{id}` | Get report by ID |
| GET | `/api/reports/status/{status}` | Get reports by status |
| GET | `/api/reports/recent/{hours}` | Get recent reports |
| GET | `/api/reports/stats/count` | Get report statistics |
| POST | `/api/reports` | Create new report |
| PUT | `/api/reports/{id}/status` | Update report status |
| DELETE | `/api/reports/{id}` | Delete report |

### Task Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tasks` | Get all tasks |
| GET | `/api/tasks/{id}` | Get task by ID |
| GET | `/api/tasks/status/{status}` | Get tasks by status |
| GET | `/api/tasks/frequency/{frequency}` | Get tasks by frequency |
| GET | `/api/tasks/stats/count` | Get task statistics |
| POST | `/api/tasks` | Create new task |
| PUT | `/api/tasks/{id}/start` | Start a task |
| PUT | `/api/tasks/{id}/complete` | Complete a task |
| PUT | `/api/tasks/{id}/fail` | Mark task as failed |

## 📝 API Usage Examples

### Create a Report
```bash
curl -X POST http://localhost:8080/api/reports \
  -H "Content-Type: application/json" \
  -d '{"reportName": "Custom Report", "generatedBy": "API"}'
```

### Get All Reports
```bash
curl http://localhost:8080/api/reports
```

### Get Reports by Status
```bash
curl http://localhost:8080/api/reports/status/COMPLETED
```

### Update Report Status
```bash
curl -X PUT http://localhost:8080/api/reports/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "COMPLETED", "content": "Report content here"}'
```

### Create a Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"taskName": "Daily Sync", "taskDescription": "Sync data daily", "executionFrequency": "DAILY"}'
```

## 🗄️ Database

The application uses **H2 in-memory database** for development and testing.

### H2 Console
Access the H2 database console:
- URL: `http://localhost:8080/h2-console`
- Driver Class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:mem:testdb`
- User Name: `sa`
- Password: (leave blank)

## 🔧 Configuration

Edit `src/main/resources/application.properties` to customize:

```properties
# Server port
server.port=8080

# Scheduling pool size
spring.task.scheduling.pool.size=5

# Logging level
logging.level.com.example=DEBUG
```

## 📊 Report & Task Status

### Report Status
- **PENDING**: Report created, waiting to be processed
- **IN_PROGRESS**: Report is currently being generated
- **COMPLETED**: Report generation successful
- **FAILED**: Report generation encountered an error

### Task Status
- **PENDING**: Task created, not started
- **IN_PROGRESS**: Task is currently executing
- **COMPLETED**: Task executed successfully
- **FAILED**: Task execution failed
- **SKIPPED**: Task was skipped

## 🔍 Logging

The application uses SLF4J with Logback for comprehensive logging.

**Log Levels**:
- `DEBUG`: Detailed information for debugging
- `INFO`: General informational messages
- `WARN`: Warning messages
- `ERROR`: Error messages

## 🧪 Testing the Scheduler

1. **Start the application**
   ```bash
   mvn spring-boot:run
   ```

2. **Monitor logs** to see scheduled tasks executing

3. **Check the database** via H2 console

4. **Query API endpoints** to verify data creation
   ```bash
   curl http://localhost:8080/api/reports/stats/count
   ```

## 📚 Cron Expression Reference

| Expression | Meaning |
|------------|----------|
| `0 0 2 * * ?` | Daily at 2:00 AM |
| `0 0 9 ? * MON` | Monday at 9:00 AM |
| `0 0 8 1 * ?` | 1st of month at 8:00 AM |
| `0 */15 * * * ?` | Every 15 minutes |
| `0 0 */4 * * ?` | Every 4 hours |
| `0 0 0 * * ?` | Daily at midnight |

## 🛠️ Technologies Used

- **Spring Boot 3.1.5**
- **Spring Data JPA**
- **Spring Scheduling**
- **H2 Database**
- **Lombok**
- **Maven**
- **Java 17**

## 📦 Dependencies

```xml
<!-- Spring Boot Web -->
spring-boot-starter-web

<!-- Spring Data JPA -->
spring-boot-starter-data-jpa

<!-- H2 Database -->
h2

<!-- Lombok -->
lombok
```

## 🐛 Troubleshooting

### Application won't start
- Ensure Java 17+ is installed: `java -version`
- Check Maven version: `mvn -version`
- Clean and rebuild: `mvn clean install`

### Scheduled tasks not executing
- Verify `@EnableScheduling` is present in main class
- Check logs for errors
- Ensure `spring.task.scheduling.pool.size > 0` in properties

### Database connection issues
- Verify H2 driver is in classpath
- Check `application.properties` for correct URL
- Access H2 console to verify database state

## 📄 License

This project is licensed under the MIT License.

## 👤 Author

PavanKumarACTI

## 📞 Support

For issues and questions, please open an issue in the repository.
