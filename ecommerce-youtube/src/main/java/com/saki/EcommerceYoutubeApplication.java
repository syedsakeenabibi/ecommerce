package com.saki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.saki")  // Specify the base package(s) to scan for components
@EntityScan(basePackages = "com.saki.model")  // Specify the base package(s) to scan for entities
public class EcommerceYoutubeApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceYoutubeApplication.class, args);
    }
}
