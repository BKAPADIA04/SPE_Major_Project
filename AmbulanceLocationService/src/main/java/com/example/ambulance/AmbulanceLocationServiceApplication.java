package com.example.ambulance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class AmbulanceLocationServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(AmbulanceLocationServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AmbulanceLocationServiceApplication.class, args);
        logger.info("🚑 AmbulanceLocationServiceApplication has started!");
    }

    @PostConstruct
    public void onStartup() {
        logger.info("🚑 AmbulanceLocationServiceApplication has started!");
    }
}
