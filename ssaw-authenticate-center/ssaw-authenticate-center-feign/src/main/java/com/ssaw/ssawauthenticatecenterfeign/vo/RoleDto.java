package com.ssaw.ssawauthenticatecenterfeign.vo;

import com.ssaw.commons.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author HuSen.
 * @date 2018/12/14 19:11.
 */
@Setter
@Getter
public class RoleDto extends BaseDto {

    @NotBlank(message = "角色名称不能为空!")
    private String name;

    private String description;

    private List<TreeDto> permissions;
}
