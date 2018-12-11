package com.ssaw.ssawuserresourceservice;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.ssaw.commons.enable.EnableAutoRequestResolve;
import com.ssaw.ssawuserresourcefeign.feign.BasicFeignPackageClass;
import com.ssaw.support.annotations.EnableSccServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author HS
 */
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableSccServer
@EnableAutoRequestResolve
@EnableFeignClients(basePackageClasses = BasicFeignPackageClass.class)
@ComponentScan(basePackages = "com.ssaw")
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
		SpringApplication.run(AuthenticateCenterServiceApplication.class, args);
	}
}
