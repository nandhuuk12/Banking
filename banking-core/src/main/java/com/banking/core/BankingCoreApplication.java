package com.banking.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for Banking Core library.
 * This can be used as a standalone Spring Boot application for testing,
 * or as a library dependency in other applications.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class BankingCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingCoreApplication.class, args);
    }
}
