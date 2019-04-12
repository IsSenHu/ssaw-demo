package com.ssaw;

import com.ssaw.commons.enable.EnableAutoRequestResolve;
import com.ssaw.commons.util.app.ApplicationContextUtil;
import com.ssaw.ssawauthenticatecenterfeign.annotations.EnableSetUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author HS
 */
@Slf4j
@EnableSetUserInfo
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
@EnableAutoRequestResolve
public class AuthenticateCenterServiceApplication {

//	@Bean
//	public ServletRegistrationBean<HystrixMetricsStreamServlet> servletRegistrationBean() {
//		HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
//		ServletRegistrationBean<HystrixMetricsStreamServlet> servletRegistrationBean = new ServletRegistrationBean<>(streamServlet);
//		servletRegistrationBean.setLoadOnStartup(1);
//		servletRegistrationBean.addUrlMappings("/actuator/hystrix.stream");
//		servletRegistrationBean.setName("HystrixMetricsStreamServlet");
//		return servletRegistrationBean;
//	}

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(AuthenticateCenterServiceApplication.class, args);
		ApplicationContextUtil.setContext(run);
	}
}
