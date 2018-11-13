package com.ssaw.ssawhystrixmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author HS
 */
@SpringBootApplication
@EnableHystrixDashboard
@EnableDiscoveryClient
@EnableCircuitBreaker
public class HystrixMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixMonitorApplication.class, args);
    }
}
