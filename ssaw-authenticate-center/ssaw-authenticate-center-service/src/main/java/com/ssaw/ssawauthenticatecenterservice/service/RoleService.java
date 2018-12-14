package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.PermissionDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.RoleDto;
import com.ssaw.ssawauthenticatecenterservice.dto.RolePermissionReqDto;

import java.util.List;

/**
 * @author HuSen.
 * @date 2018/12/14 17:53.
 */
public interface RoleService {
    CommonResult<RoleDto> add(RoleDto roleDto);

    CommonResult<RoleDto> findById(Long id);

    TableData<RoleDto> page(PageReqDto<RoleDto> pageReqDto);

    CommonResult<RoleDto> update(RoleDto roleDto);

    CommonResult<List<PermissionDto>> findAllPermissionByRoleId(Long id);

    CommonResult<Long> changeRolePermission(RolePermissionReqDto reqDto);
}
