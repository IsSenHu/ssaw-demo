package com.ssaw.ssawauthenticatecenterfeign.config;

import com.ssaw.ssawauthenticatecenterfeign.interceptor.SetUserInfoInterceptor;
import com.ssaw.ssawauthenticatecenterfeign.properties.EnableResourceAutoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author HuSen
 * @date 2019/3/1 10:03
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Autowired
    public InterceptorConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     *添加拦截器
     * @param registry 拦截注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        EnableResourceAutoProperties properties = applicationContext.getBean(EnableResourceAutoProperties.class);
        registry.addInterceptor(new SetUserInfoInterceptor()).addPathPatterns("/**")
                // 排除白名单
                .excludePathPatterns(properties.getWhiteList());
    }
}