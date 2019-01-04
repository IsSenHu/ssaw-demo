package com.ssaw.ssawauthenticatecenterservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author HuSen.
 * @date 2018/11/28 10:54.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("SSAW-USER-RESOURCE");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers().antMatchers("/api/**")
                .and().authorizeRequests()
                .antMatchers("/api/user/get/*", "/api/user/page").access("#oauth2.hasScope('USER_READ')")
                .antMatchers("/api/roles/get/*").access("#oauth2.hasScope('ROLE_READ')")
                .antMatchers("/api/user/save").access("#oauth2.hasScope('USER_WRITE')")
                .antMatchers("/api/role/save").access("#oauth2.hasScope('ROLE_WRITE')")
                .antMatchers("/api/user/saveUserRoles/*").access("#oauth2.hasScope('USER_ROLE_WRITE')")
                .and().csrf().disable();
    }
}
