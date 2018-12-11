package com.ssaw.ssawuserresourceservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.ResourceDto;

/**
 * @author HuSen.
 * @date 2018/12/11 14:14.
 */
public interface ResourceService {

    CommonResult<ResourceDto> findByMark(String mark);

    CommonResult<ResourceDto> save(ResourceDto resourceDto);

    CommonResult<String> bind(Integer resourceId, Integer serviceId);
}
