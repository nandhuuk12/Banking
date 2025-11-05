package com.banking.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // Should be configured by consuming application
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for Banking Core library.
 * This can be used as a standalone Spring Boot application for testing,
 * or as a library dependency in other applications.
 */
@SpringBootApplication
@EnableTransactionManagement
public class BankingCoreApplication {
    // Note: @EnableJpaAuditing should be configured by the consuming application

    public static void main(String[] args) {
        SpringApplication.run(BankingCoreApplication.class, args);
    }
}
