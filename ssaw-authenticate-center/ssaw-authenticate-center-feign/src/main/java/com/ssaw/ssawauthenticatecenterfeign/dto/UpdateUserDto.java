package com.ssaw.ssawauthenticatecenterfeign.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
public class UpdateUserDto implements Serializable {
    /**
     * 主键
     **/
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空!")
    @Pattern(regexp = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]{1,50}+$", message = "用户名只能由英文字母、数字、中文、下划线1到50位字符组成!")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空!")
    @Pattern(regexp = ".{6,20}$", message = "密码由6-20位字符组成!")
    private String password;

    /**
     * 是否启用
     */
    private Boolean isEnable;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户描述
     */
    private String description;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 用户角色ID
     * */
    @NotNull(message = "请选择用户角色")
    private Long roleId;

    /**
     * 用户角色名称
     * */
    private String roleName;
}
