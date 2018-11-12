package com.ssaw.ssawturbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;

/**
 * @author HS
 */
@EnableAutoConfiguration
@EnableTurbineStream
@EnableDiscoveryClient
@SpringBootApplication
public class SsawTurbineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsawTurbineApplication.class, args);
    }
}
