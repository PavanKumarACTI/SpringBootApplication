package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot Application class with scheduling enabled
 * 
 * @EnableScheduling - Enables support for scheduled task execution
 */
@SpringBootApplication
@EnableScheduling
public class SpringBootSchedulingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSchedulingApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("Spring Boot Scheduling Application Started");
        System.out.println("Scheduled tasks are now active!");
        System.out.println("========================================\n");
    }
}
