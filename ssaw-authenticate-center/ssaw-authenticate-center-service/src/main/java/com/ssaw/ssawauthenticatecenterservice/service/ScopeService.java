package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.vo.ScopeDto;

import java.util.List;

/**
 * @author HuSen.
 * @date 2018/12/12 11:38.
 */
public interface ScopeService {

    /**
     * 新增作用域
     * @param scopeDto 新增作用域请求对象
     * @return 新增结果
     */
    CommonResult<ScopeDto> add(ScopeDto scopeDto);

    /**
     * 分页查询作用域
     * @param pageReqDto 分页查询参数
     * @return 分页结果
     */
    TableData<ScopeDto> page(PageReqDto<ScopeDto> pageReqDto);

    /**
     * 根据ID删除作用域
     * @param id ID
     * @return 删除结果
     */
    CommonResult<Long> delete(Long id);

    /**
     * 根据ID查询作用域
     * @param id ID
     * @return 作用域
     */
    CommonResult<ScopeDto> findById(Long id);

    /**
     * 修改作用域
     * @param scopeDto 修改作用域请求对象
     * @return 修改结果
     */
    CommonResult<ScopeDto> update(ScopeDto scopeDto);

    /**
     * 根据作用域名称搜索作用域
     * @param scope 作用域名称
     * @return 作用域
     */
    CommonResult<List<ScopeDto>> search(String scope);

    /**
     * 根据作用域名称搜索作用域
     * @param scope 作用域名称
     * @return 作用域
     */
    CommonResult<List<ScopeDto>> searchForUpdate(String scope);

    /**
     * 上传作用域
     * @param scopeDtoList 作用域集合
     * @return 上传结果
     */
    CommonResult<String> uploadScopes(List<ScopeDto> scopeDtoList);

    /**
     * 刷新作用域缓存
     * @param source 来源
     */
    void refreshScope(String source);
}
