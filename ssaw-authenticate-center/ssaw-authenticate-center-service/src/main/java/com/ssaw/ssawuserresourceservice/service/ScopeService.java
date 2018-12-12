package com.ssaw.ssawuserresourceservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.ScopeDto;

/**
 * @author HuSen.
 * @date 2018/12/12 11:38.
 */
public interface ScopeService {

    CommonResult<ScopeDto> add(ScopeDto scopeDto);
}
