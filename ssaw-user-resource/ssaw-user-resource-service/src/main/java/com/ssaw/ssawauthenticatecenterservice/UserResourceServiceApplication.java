package com.ssaw.ssawauthenticatecenterservice;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.ssaw.commons.annotations.EnableInnerSecurity;
import com.ssaw.commons.enable.EnableAutoRequestResolve;
import com.ssaw.commons.util.app.ApplicationContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @author HS
 */
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableInnerSecurity
@EnableAutoRequestResolve
public class UserResourceServiceApplication {

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
		ApplicationContextUtil.applicationContext = SpringApplication.run(UserResourceServiceApplication.class, args);
	}
}
