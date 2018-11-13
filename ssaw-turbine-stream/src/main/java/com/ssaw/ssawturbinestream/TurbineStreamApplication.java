package com.ssaw.ssawturbinestream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;

/**
 * @author HS
 */
@SpringBootApplication
@EnableTurbineStream()
public class TurbineStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(TurbineStreamApplication.class, args);
    }
}