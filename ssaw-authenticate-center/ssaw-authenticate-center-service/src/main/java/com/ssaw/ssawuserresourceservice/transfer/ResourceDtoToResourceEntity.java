package com.ssaw.ssawuserresourceservice.transfer;

import com.ssaw.ssawuserresourcefeign.dto.ResourceDto;
import com.ssaw.ssawuserresourceservice.entity.ResourceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.function.Function;

/**
 * @author HuSen.
 * @date 2018/12/11 19:12.
 */
@Component
public class ResourceDtoToResourceEntity implements Function<ResourceDto, ResourceEntity> {

    private final ServiceDtoToServiceEntity serviceDtoToServiceEntity;

    @Autowired
    public ResourceDtoToResourceEntity(ServiceDtoToServiceEntity serviceDtoToServiceEntity) {
        this.serviceDtoToServiceEntity = serviceDtoToServiceEntity;
    }

    @Override
    public ResourceEntity apply(ResourceDto resourceDto) {
        ResourceEntity resourceEntity = null;
        if(null != resourceDto) {
            resourceEntity = new ResourceEntity();
            resourceEntity.setId(resourceDto.getId());
            resourceEntity.setUniqueMark(resourceDto.getUniqueMark());
            resourceEntity.setService(serviceDtoToServiceEntity.apply(resourceDto.getService()));
        }
        return resourceEntity;
    }
}
