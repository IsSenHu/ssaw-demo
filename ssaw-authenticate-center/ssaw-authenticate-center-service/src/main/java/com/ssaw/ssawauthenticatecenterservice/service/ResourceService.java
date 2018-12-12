package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.ResourceDto;

/**
 * @author HuSen.
 * @date 2018/12/11 14:14.
 */
public interface ResourceService {

    CommonResult<ResourceDto> add(ResourceDto resourceDto);

    TableData<ResourceDto> page(PageReqDto<ResourceDto> pageReqDto);

    CommonResult<ResourceDto> findById(Long id);

    CommonResult<ResourceDto> update(ResourceDto resourceDto);

    CommonResult<Long> delete(Long id);
}
