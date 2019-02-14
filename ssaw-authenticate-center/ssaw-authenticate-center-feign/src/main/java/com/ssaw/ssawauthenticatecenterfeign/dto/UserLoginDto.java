package com.ssaw.ssawauthenticatecenterfeign.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户登录请求VO
 * @author HuSen
 * @date 2019/02/13
 */
@Getter
@Setter
public class UserLoginDto implements Serializable {

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;
}
