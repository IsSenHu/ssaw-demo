package com.ssaw.ssawauthenticatecenterservice.constants.client;

import java.util.HashSet;
import java.util.Set;

/**
 * @author HuSen.
 * @date 2019/1/4 13:55.
 */
public class ClientConstant {

    public static final String CLIENT_PREFIX = "client";
    public static final String BEARER = "bearer";
    public static final Set<String> CODE = new HashSet<>(1);
    public static final int LOGIN_TIME = 86400;

    static {
        CODE.add("code");
    }

    /**
     * oauth2授权方式
     */
    public enum AuthorizedGrantTypes {
        // 授权码模式
        AUTHORIZATION_CODE("authorization_code"),
        // 密码模式
        PASSWORD("password"),
        // Refresh Token
        REFRESH_TOKEN("refresh_token"),
        // 客户端模式
        CLIENT_CREDENTIALS("client_credentials"),
        // 简化模式
        IMPLICIT("implicit");
        private String value;

        AuthorizedGrantTypes(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
