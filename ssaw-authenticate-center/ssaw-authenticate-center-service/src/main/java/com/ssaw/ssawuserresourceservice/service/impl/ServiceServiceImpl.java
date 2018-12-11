package com.ssaw.ssawuserresourceservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.ServiceDto;
import com.ssaw.ssawuserresourceservice.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author HuSen.
 * @date 2018/12/11 13:42.
 */
@Slf4j
@Service
public class ServiceServiceImpl implements ServiceService {

    @Override
    public CommonResult<ServiceDto> findByServiceName(String serviceName) {
        return null;
    }

    @Override
    public CommonResult<ServiceDto> save(ServiceDto serviceDto) {
        return null;
    }
}
