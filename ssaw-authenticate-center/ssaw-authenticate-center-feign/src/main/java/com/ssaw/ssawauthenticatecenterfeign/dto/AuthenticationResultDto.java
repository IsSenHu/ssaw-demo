package com.ssaw.ssawauthenticatecenterfeign.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 认证结果Dto
 * @author HuSen.
 * @date 2019/1/23 10:14.
 */
@Getter
@Setter
public class AuthenticationResultDto implements Serializable {
    /** 状态码 */
    private int code;
    /** 错误信息 */
    private String error;

    @SuppressWarnings("WeakerAccess")
    public static class Constant {
        private static final int SUCCESS = 0;
        private static final int FAIL = 1;
    }

    public static AuthenticationResultDto success() {
        AuthenticationResultDto dto = new AuthenticationResultDto();
        dto.setCode(Constant.SUCCESS);
        return dto;
    }

    public static AuthenticationResultDto fail(String error) {
        AuthenticationResultDto dto = new AuthenticationResultDto();
        dto.setCode(Constant.FAIL);
        dto.setError(error);
        return dto;
    }
}
