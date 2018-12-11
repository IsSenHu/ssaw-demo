package com.ssaw.ssawuserresourceservice.transfer;

import com.ssaw.ssawuserresourcefeign.dto.ServiceDto;
import com.ssaw.ssawuserresourceservice.entity.ServiceEntity;
import org.springframework.stereotype.Component;
import java.util.function.Function;

/**
 * @author HuSen.
 * @date 2018/12/11 16:52.
 */
@Component
public class ServiceEntityToServiceDto implements Function<ServiceEntity, ServiceDto> {

    @Override
    public ServiceDto apply(ServiceEntity serviceEntity) {
        ServiceDto serviceDto = null;
        if (serviceEntity != null) {
            serviceDto = new ServiceDto();
            serviceDto.setId(serviceEntity.getId());
            serviceDto.setServiceName(serviceEntity.getServiceName());
            serviceDto.setScopes(serviceEntity.getScopes());
            serviceDto.setCreateTime(serviceEntity.getCreateTime());
            serviceDto.setCreateMan(serviceEntity.getCreateMan());
            serviceDto.setUpdateTime(serviceEntity.getUpdateTime());
            serviceDto.setUpdateMan(serviceEntity.getUpdateMan());
            serviceDto.setIsBind(serviceEntity.getIsBind());
        }
        return serviceDto;
    }
}
