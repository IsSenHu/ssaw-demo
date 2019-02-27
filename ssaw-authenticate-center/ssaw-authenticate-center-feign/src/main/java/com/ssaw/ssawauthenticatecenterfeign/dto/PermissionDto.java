package com.ssaw.ssawauthenticatecenterfeign.dto;

import com.ssaw.commons.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author HuSen.
 * @date 2018/12/13 17:11.
 */
@Setter
@Getter
public class PermissionDto extends BaseDto {

    /** 权限名字 */
    @NotBlank(message = "权限名字不能为空!")
    private String name;

    /** 所关联作用域ID */
    @NotNull(message = "所属作用域不能为空!")
    private Long scopeId;

    private String scopeName;

    /** 所关联资源ID */
    private Long resourceId;

    private String resourceName;

    /** 描述 */
    private String description;
}
