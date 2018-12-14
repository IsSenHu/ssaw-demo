package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;

import java.util.List;

/**
 * @author HuSen.
 * @date 2018/12/12 11:38.
 */
public interface ScopeService {

    CommonResult<ScopeDto> add(ScopeDto scopeDto);

    TableData<ScopeDto> page(PageReqDto<ScopeDto> pageReqDto);

    CommonResult<Long> delete(Long id);

    CommonResult<ScopeDto> findById(Long id);

    CommonResult<ScopeDto> update(ScopeDto scopeDto);

    CommonResult<List<ScopeDto>> search(String scopeId);
}
