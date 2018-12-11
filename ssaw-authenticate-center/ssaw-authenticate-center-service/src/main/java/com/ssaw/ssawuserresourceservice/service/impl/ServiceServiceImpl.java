package com.ssaw.ssawuserresourceservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.ServiceDto;
import com.ssaw.ssawuserresourceservice.entity.ServiceEntity;
import com.ssaw.ssawuserresourceservice.repository.ServiceRepository;
import com.ssaw.ssawuserresourceservice.service.ServiceService;
import com.ssaw.ssawuserresourceservice.transfer.ServiceDtoToServiceEntity;
import com.ssaw.ssawuserresourceservice.transfer.ServiceEntityToServiceDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import static com.ssaw.commons.constant.Constants.ResultCodes.*;

/**
 * @author HuSen.
 * @date 2018/12/11 13:42.
 */
@Slf4j
@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceEntityToServiceDto serviceEntityToServiceDto;
    private final ServiceDtoToServiceEntity serviceDtoToServiceEntity;
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceServiceImpl(ServiceDtoToServiceEntity serviceDtoToServiceEntity, ServiceRepository serviceRepository, ServiceEntityToServiceDto serviceEntityToServiceDto) {
        this.serviceDtoToServiceEntity = serviceDtoToServiceEntity;
        this.serviceRepository = serviceRepository;
        this.serviceEntityToServiceDto = serviceEntityToServiceDto;
    }

    @Override
    public CommonResult<ServiceDto> findByServiceName(String serviceName) {
        if(StringUtils.isBlank(serviceName)) {
            return CommonResult.createResult(PARAM_ERROR, "服务名不能为空!", null);
        }
        ServiceEntity serviceEntity = serviceRepository.findByServiceName(serviceName);
        if(serviceEntity == null) {
            return CommonResult.createResult(DATA_NOT_EXIST, "该服务不存在!", null);
        }
        return CommonResult.createResult(SUCCESS, "成功!", serviceEntityToServiceDto.apply(serviceEntity));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<ServiceDto> save(ServiceDto serviceDto) {
        if(null == serviceDto.getId()) {
            if(serviceRepository.countByServiceName(serviceDto.getServiceName()) > 0) {
                return CommonResult.createResult(DATA_EXIST, "该服务名已存在，无法添加!", serviceDto);
            }
            ServiceEntity serviceEntity = serviceDtoToServiceEntity.apply(serviceDto);
            serviceEntity.setCreateTime(LocalDateTime.now());
            serviceRepository.save(serviceEntity);
        } else {
            Optional<ServiceEntity> optional = serviceRepository.findById(serviceDto.getId());
            if(!optional.isPresent()) {
                return CommonResult.createResult(DATA_NOT_EXIST, "该服务名不存在，无法更新!", serviceDto);
            }
            ServiceEntity serviceEntity = optional.get();
            if(!StringUtils.equalsIgnoreCase(serviceEntity.getServiceName(), serviceDto.getServiceName())
                    && serviceRepository.countByServiceName(serviceDto.getServiceName()) > 0) {
                return CommonResult.createResult(DATA_EXIST, "该服务名已存在，无法更新!", serviceDto);
            }
            serviceEntity.setServiceName(serviceDto.getServiceName());
            serviceEntity.setScopes(serviceDto.getScopes());
            serviceEntity.setUpdateTime(LocalDateTime.now());
            serviceRepository.save(serviceEntity);
        }
        return CommonResult.createResult(SUCCESS, "成功!", serviceDto);
    }
}
