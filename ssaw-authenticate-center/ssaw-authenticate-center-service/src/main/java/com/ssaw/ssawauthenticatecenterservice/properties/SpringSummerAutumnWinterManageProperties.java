package com.ssaw.ssawauthenticatecenterservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author HuSen
 * @date 2019/3/1 10:33
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ssaw.manage")
public class SpringSummerAutumnWinterManageProperties {

    /** 管理客户端ID */
    private String clientId;
    /** 管理客户端密码 */
    private String clientSecret;
    /** 注册回调地址 */
    private String clientRegisteredRedirectUris;
    /** 过期时间 */
    private Integer clientExpire;
}