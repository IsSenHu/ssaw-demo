package com.ssaw.ssawauthenticatecenterfeign.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author HuSen.
 * @date 2019/1/23 13:51.
 */
@Setter
@Getter
public class EditRoleDto implements Serializable {
    private RoleDto roleDto;

    private List<TreeDto> treeDtos;

    private List<Long> defaultExpandedKeys;

    private List<Long> defaultCheckedKeys;
}
