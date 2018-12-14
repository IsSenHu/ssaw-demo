package com.ssaw.ssawauthenticatecenterfeign.dto;

import com.ssaw.commons.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HuSen.
 * @date 2018/12/14 19:11.
 */
@Setter
@Getter
public class RoleDto extends BaseDto {

    private String name;

    private String description;
}
