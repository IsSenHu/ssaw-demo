package com.ssaw.ssawui.config;

import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author hszyp
 */
@EnableFeignClients(basePackages = {"com.ssaw.ssawauthenticatecenterfeign.feign"})
public class EnableFeignAutoConfiguration {
}
