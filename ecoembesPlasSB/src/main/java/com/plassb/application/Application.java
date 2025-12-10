package com.plassb.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.plassb")
@EntityScan(basePackages = "com.plassb.domain")
@EnableJpaRepositories(basePackages = "com.plassb.repositorio")
public class Application {

    public static void main(String[] args) {
        System.out.println("    SERVIDOR PLASSB INICIADO");
        System.out.println(" Puerto: 8081");
        System.out.println("  Base de datos: H2 (jdbc:h2:mem:plassbdb)");
        System.out.println(" Fecha: " + java.time.LocalDateTime.now());
        
        SpringApplication.run(Application.class, args);
    }
}