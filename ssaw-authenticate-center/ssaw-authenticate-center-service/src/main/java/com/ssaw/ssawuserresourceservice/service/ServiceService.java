package com.ssaw.ssawuserresourceservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.ServiceDto;

/**
 * @author HuSen.
 * @date 2018/12/11 13:42.
 */
public interface ServiceService {
    CommonResult<ServiceDto> findByServiceName(String serviceName);

    CommonResult<ServiceDto> save(ServiceDto serviceDto);
}
