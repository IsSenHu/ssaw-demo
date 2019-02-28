package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.vo.PermissionDto;

/**
 * @author HuSen.
 * @date 2018/12/13 17:04.
 */
public interface PermissionService {
    /**
     * 新增权限
     * @param permissionDto 新增权限请求对象
     * @return 新增结果
     */
    CommonResult<PermissionDto> add(PermissionDto permissionDto);

    /**
     * 分页查询权限
     * @param pageReqDto 分页查询参数
     * @return 分页结果
     */
    TableData<PermissionDto> page(PageReqDto<PermissionDto> pageReqDto);

    /**
     * 根据ID删除权限
     * @param id ID
     * @return 删除结果
     */
    CommonResult<Long> delete(Long id);

    /**
     * 修改权限
     * @param permissionDto 修改请求对象
     * @return 修改结果
     */
    CommonResult<PermissionDto> update(PermissionDto permissionDto);

    /**
     * 根据ID查询权限
     * @param id ID
     * @return 权限
     */
    CommonResult<PermissionDto> findById(Long id);
}
