package com.ssaw.smscenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SmsCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsCenterApplication.class, args);
    }
}

