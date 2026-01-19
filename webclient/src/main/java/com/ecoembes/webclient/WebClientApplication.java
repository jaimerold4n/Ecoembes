package com.ecoembes.webclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal del cliente web Ecoembes
 */
@SpringBootApplication
public class WebClientApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WebClientApplication.class, args);
    }
}