package com.ssaw.smscenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ImportResource("classpath:application-dubbo.xml")
public class SmsCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsCenterApplication.class, args);
    }
}

