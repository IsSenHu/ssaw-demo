package com.ssaw.ssawuserresourceservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.RoleDto;

import java.util.List;

/**
 * @author HuSen.
 * @date 2018/11/27 19:01.
 */
public interface RoleService {
    CommonResult<List<RoleDto>> findAllRoleByUserId(Long userId);

    CommonResult<RoleDto> save(RoleDto roleDto);

    CommonResult<Long> delete(Long roleId);
}
