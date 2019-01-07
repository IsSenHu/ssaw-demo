package com.ssaw;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import com.ssaw.commons.config.InnerSecurityConfig;
import com.ssaw.commons.enable.EnableAutoRequestResolve;
import com.ssaw.commons.enable.EnableFeignHeader;
import com.ssaw.ssawuserresourcefeign.feign.BasicFeignPackageClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author HS
 */
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableAutoRequestResolve
@EnableFeignClients(basePackageClasses = BasicFeignPackageClass.class)
@EnableFeignHeader
@ComponentScan(basePackages = "com.ssaw")
@Slf4j
@Import(InnerSecurityConfig.class)
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
