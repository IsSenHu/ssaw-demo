package com.ssaw.ssawmehelper;

import com.ssaw.commons.annotations.EnableSnowFlake;
import com.ssaw.commons.enable.EnableAutoRequestResolve;
import com.ssaw.ssawauthenticatecenterfeign.annotations.EnableAutoAuthenticateInfo;
import com.ssaw.ssawauthenticatecenterfeign.annotations.EnableSetUserInfo;
import com.ssaw.support.uaa.annotation.EnableUaa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hszyp
 */
@EnableSetUserInfo
@EnableAutoAuthenticateInfo
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableAutoRequestResolve
@SpringBootApplication
@EnableFeignClients(basePackages = "com.ssaw")
@EnableScheduling
@EnableSnowFlake
//@EnableUaa
public class SsawMeHelperApplication {

//    @Bean
//    public ServletRegistrationBean<HystrixMetricsStreamServlet> servletRegistrationBean() {
//        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
//        ServletRegistrationBean<HystrixMetricsStreamServlet> servletRegistrationBean = new ServletRegistrationBean<>(streamServlet);
//        servletRegistrationBean.setLoadOnStartup(1);
//        servletRegistrationBean.addUrlMappings("/actuator/hystrix.stream");
//        servletRegistrationBean.setName("HystrixMetricsStreamServlet");
//        return servletRegistrationBean;
//    }

    public static void main(String[] args) {
        SpringApplication.run(SsawMeHelperApplication.class, args);
    }

}
