package com.ssaw;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.ssaw.commons.enable.EnableAutoRequestResolve;
import com.ssaw.commons.enable.EnableFeignHeader;
import com.ssaw.commons.util.app.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author HS
 */
@Slf4j
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableAutoRequestResolve
@EnableFeignHeader
@EnableFeignClients(basePackages = "com.ssaw")
public class AuthenticateCenterServiceApplication {

	@Bean
	public ServletRegistrationBean<HystrixMetricsStreamServlet> servletRegistrationBean() {
		HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
		ServletRegistrationBean<HystrixMetricsStreamServlet> servletRegistrationBean = new ServletRegistrationBean<>(streamServlet);
		servletRegistrationBean.setLoadOnStartup(1);
		servletRegistrationBean.addUrlMappings("/actuator/hystrix.stream");
		servletRegistrationBean.setName("HystrixMetricsStreamServlet");
		return servletRegistrationBean;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(AuthenticateCenterServiceApplication.class, args);
		ApplicationContextUtil.setContext(run);
	}
}
