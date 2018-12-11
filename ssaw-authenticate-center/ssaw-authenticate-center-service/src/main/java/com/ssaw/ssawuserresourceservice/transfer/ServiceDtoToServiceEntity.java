package com.ssaw.ssawuserresourceservice.transfer;

import com.ssaw.ssawuserresourcefeign.dto.ServiceDto;
import com.ssaw.ssawuserresourceservice.entity.ServiceEntity;
import org.springframework.stereotype.Component;
import java.util.function.Function;

/**
 * @author HuSen.
 * @date 2018/12/11 16:26.
 */
@Component
public class ServiceDtoToServiceEntity implements Function<ServiceDto, ServiceEntity> {
    @Override
    public ServiceEntity apply(ServiceDto serviceDto) {
        ServiceEntity serviceEntity = null;
        if(null != serviceDto) {
            serviceEntity = new ServiceEntity();
            serviceEntity.setId(serviceDto.getId());
            serviceEntity.setScopes(serviceDto.getScopes());
            serviceEntity.setServiceName(serviceDto.getServiceName());
            serviceEntity.setIsBind(serviceDto.getIsBind());
        }
        return serviceEntity;
    }
}
