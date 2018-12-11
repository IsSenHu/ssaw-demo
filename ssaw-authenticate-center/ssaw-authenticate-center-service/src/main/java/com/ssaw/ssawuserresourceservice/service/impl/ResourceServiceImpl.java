package com.ssaw.ssawuserresourceservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.ResourceDto;
import com.ssaw.ssawuserresourceservice.entity.ResourceEntity;
import com.ssaw.ssawuserresourceservice.entity.ServiceEntity;
import com.ssaw.ssawuserresourceservice.repository.ResourceRepository;
import com.ssaw.ssawuserresourceservice.repository.ServiceRepository;
import com.ssaw.ssawuserresourceservice.service.ResourceService;
import com.ssaw.ssawuserresourceservice.transfer.ResourceDtoToResourceEntity;
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
 * @date 2018/12/11 14:14.
 */
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceDtoToResourceEntity resourceDtoToResourceEntity;

    private final ResourceRepository resourceRepository;

    private final ServiceRepository serviceRepository;

    @Autowired
    public ResourceServiceImpl(ResourceDtoToResourceEntity resourceDtoToResourceEntity, ResourceRepository resourceRepository, ServiceRepository serviceRepository) {
        this.resourceDtoToResourceEntity = resourceDtoToResourceEntity;
        this.resourceRepository = resourceRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public CommonResult<ResourceDto> findByMark(String mark) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<ResourceDto> save(ResourceDto resourceDto) {
        if(resourceDto.getId() == null) {
            ResourceEntity resourceEntity = resourceDtoToResourceEntity.apply(resourceDto);
            ServiceEntity serviceEntity = resourceEntity.getService();
            if(serviceEntity != null && serviceEntity.getId() != null) {
                Optional<ServiceEntity> optionalResourceEntity = serviceRepository.findById(serviceEntity.getId());
                optionalResourceEntity.ifPresent(service -> {
                    service.setIsBind(true);
                    resourceEntity.setService(service);
                });
            } else if(serviceEntity != null && StringUtils.isNotBlank(serviceEntity.getServiceName())
                    && serviceRepository.countByServiceName(serviceEntity.getServiceName()) > 0) {
                return CommonResult.createResult(PARAM_ERROR, "关联的服务已存在!", resourceDto);
            } else if(serviceEntity != null && StringUtils.isBlank(serviceEntity.getServiceName())) {
                return CommonResult.createResult(PARAM_ERROR, "关联的服务名称为空!", resourceDto);
            } else if(resourceRepository.countByUniqueMark(resourceEntity.getUniqueMark()) > 0) {
                return CommonResult.createResult(DATA_EXIST, "该资源名已存在!", resourceDto);
            }
            resourceEntity.setCreateTime(LocalDateTime.now());
            resourceRepository.save(resourceEntity);
        } else {
            Optional<ResourceEntity> optionalResourceEntity = resourceRepository.findById(resourceDto.getId());
            if(!optionalResourceEntity.isPresent()) {
                return CommonResult.createResult(DATA_NOT_EXIST, "该资源不存在!", resourceDto);
            }
            ResourceEntity resourceEntity = optionalResourceEntity.get();
            if(!StringUtils.equals(resourceDto.getUniqueMark(), resourceEntity.getUniqueMark())
                    && resourceRepository.countByUniqueMark(resourceDto.getUniqueMark()) > 0) {
                return CommonResult.createResult(DATA_EXIST, "该资源名已存在!", resourceDto);
            }
            resourceEntity.setUniqueMark(resourceDto.getUniqueMark());
            resourceEntity.setUpdateTime(LocalDateTime.now());
            resourceRepository.save(resourceEntity);
        }
        return CommonResult.createResult(SUCCESS, "成功!", resourceDto);
    }

    @Override
    public CommonResult<String> bind(Integer resourceId, Integer serviceId) {
        return null;
    }
}
