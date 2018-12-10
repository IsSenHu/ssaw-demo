package com.ssaw.ssawsso;

import com.ssaw.commons.enable.EnableFeignHeader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author HuSen
 * @date 2018-12-06
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.ssaw")
@EnableFeignHeader
@EnableResourceServer
@ComponentScan(basePackages = "com.ssaw")
public class SsawSsoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsawSsoApplication.class, args);
    }
}
