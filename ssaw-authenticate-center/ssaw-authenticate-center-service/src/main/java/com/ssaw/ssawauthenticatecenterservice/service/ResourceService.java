package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.vo.EditClientScopeDto;
import com.ssaw.ssawauthenticatecenterfeign.vo.ResourceDto;

import java.util.List;

/**
 * @author HuSen.
 * @date 2018/12/11 14:14.
 */
public interface ResourceService {

    /**
     * 新增资源服务
     * @param resourceDto 新增资源服务请求对象
     * @return 新增结果
     */
    CommonResult<ResourceDto> add(ResourceDto resourceDto);

    /**
     * 分页查询资源服务
     * @param pageReqDto 分页查询参数
     * @return 分页结果
     */
    TableData<ResourceDto> page(PageReqDto<ResourceDto> pageReqDto);

    /**
     * 根据ID查询资源服务
     * @param id ID
     * @return 资源服务
     */
    CommonResult<ResourceDto> findById(Long id);

    /**
     * 修改资源服务
     * @param resourceDto 修改资源请求对象
     * @return 修改结果
     */
    CommonResult<ResourceDto> update(ResourceDto resourceDto);

    /**
     * 根据ID删除资源服务
     * @param id ID
     * @return 删除结果
     */
    CommonResult<Long> delete(Long id);

    /**
     * 根据资源ID查询资源
     * @param resourceId 资源ID
     * @return 资源服务
     */
    CommonResult<List<ResourceDto>> search(String resourceId);

    /**
     * 查询所有的资源服务
     * @return 所有的资源服务
     */
    CommonResult<List<ResourceDto>> findAll();

    /**
     * 根据资源ID查询出树结构作用域
     * @param ids 资源IDS
     * @return 树结构作用域
     */
    CommonResult<EditClientScopeDto> findAllScopeByResourceIds(String ids);

    /**
     * 上传资源服务
     * @param resourceDto 上传资源服务请求对象
     * @return 上传结果
     */
    CommonResult<ResourceDto> uploadResource(ResourceDto resourceDto);
}
