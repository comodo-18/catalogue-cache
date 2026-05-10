package com.anurag.personalproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // This is the magic annotation that enables Auto-Configuration and Component Scanning
public class CatalogCacheApplication {

    public static void main(String[] args) {
        // This launches the Spring application context
        SpringApplication.run(CatalogCacheApplication.class, args);
    }
}