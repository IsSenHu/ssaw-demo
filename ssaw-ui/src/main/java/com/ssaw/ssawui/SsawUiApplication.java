package com.ssaw.ssawui;

import com.ssaw.commons.util.app.ApplicationContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author HuSen
 */
@EnableZuulProxy
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class SsawUiApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(SsawUiApplication.class, args);
		ApplicationContextUtil.setContext(run);
	}
}
