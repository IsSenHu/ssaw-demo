package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.vo.EditRoleDto;
import com.ssaw.ssawauthenticatecenterfeign.vo.RoleDto;

import java.util.List;

/**
 * @author HuSen.
 * @date 2018/12/14 17:53.
 */
public interface RoleService {

    /**
     * 新增角色
     * @param roleDto 新增角色请求对象
     * @return 新增结果
     */
    CommonResult<RoleDto> add(RoleDto roleDto);

    /**
     * 根据ID查询角色
     * @param id ID
     * @return 角色
     */
    CommonResult<EditRoleDto> findById(Long id);

    /**
     * 分页查询角色
     * @param pageReqDto 分页查询参数
     * @return 分页结果
     */
    TableData<RoleDto> page(PageReqDto<RoleDto> pageReqDto);

    /**
     * 修改角色
     * @param roleDto 修改角色请求对象
     * @return 修改结果
     */
    CommonResult<RoleDto> update(RoleDto roleDto);

    /**
     * 根据ID删除角色
     * @param id ID
     * @return 删除结果
     */
    CommonResult<Long> delete(Long id);

    /**
     * 根据角色名搜索角色
     * @param role 角色名称
     * @return 角色数据
     */
    CommonResult<List<RoleDto>> search(String role);
}
