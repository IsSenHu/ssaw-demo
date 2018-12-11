package com.ssaw.ssawuserresourceservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.ResourceDto;
import com.ssaw.ssawuserresourceservice.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author HuSen.
 * @date 2018/12/11 14:14.
 */
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    @Override
    public CommonResult<ResourceDto> findByMark(String mark) {
        return null;
    }

    @Override
    public CommonResult<ResourceDto> save(ResourceDto resourceDto) {
        return null;
    }

    @Override
    public CommonResult<String> bind(Integer resourceId, Integer serviceId) {
        return null;
    }
}
