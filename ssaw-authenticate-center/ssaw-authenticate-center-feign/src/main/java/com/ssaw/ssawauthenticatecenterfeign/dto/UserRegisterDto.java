package com.ssaw.ssawauthenticatecenterfeign.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户注册Dto
 * @author HuSen
 * @date 2019/02/14
 */
@Getter
@Setter
public class UserRegisterDto implements Serializable {
    @NotBlank(message = "用户名不能为空!")
    @Pattern(regexp = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]{1,50}+$", message = "用户名只能由英文字母、数字、中文、下划线1到50位字符组成!")
    private String username;

    @NotBlank(message = "密码不能为空!")
    @Pattern(regexp = ".{6,20}$", message = "密码由6-20位字符组成!")
    private String password;
}
