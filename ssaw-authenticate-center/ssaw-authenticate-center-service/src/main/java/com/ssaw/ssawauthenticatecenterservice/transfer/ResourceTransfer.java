package com.ssaw.ssawauthenticatecenterservice.transfer;

import com.ssaw.ssawauthenticatecenterfeign.vo.ResourceDto;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.resource.ResourceEntity;
import org.springframework.stereotype.Component;

/**
 * @author HuSen.
 * @date 2018/12/12 14:17.
 */
@Component
public class ResourceTransfer {

    public ResourceEntity dto2Entity(ResourceDto dto) {
        ResourceEntity entity = null;
        if(null != dto) {
            entity = new ResourceEntity();
            entity.setId(dto.getId());
            entity.setResourceId(dto.getResourceId());
            entity.setDescription(dto.getDescription());
            entity.setCreateTime(dto.getCreateTime());
            entity.setCreateMan(dto.getCreateMan());
            entity.setModifyTime(dto.getModifyTime());
            entity.setModifyMan(dto.getModifyMan());
        }
        return entity;
    }

    public ResourceDto entity2Dto(ResourceEntity entity) {
        ResourceDto dto = null;
        if(null != entity) {
            dto = new ResourceDto();
            dto.setId(entity.getId());
            dto.setResourceId(entity.getResourceId());
            dto.setDescription(entity.getDescription());
            dto.setCreateTime(entity.getCreateTime());
            dto.setCreateMan(entity.getCreateMan());
            dto.setModifyTime(entity.getModifyTime());
            dto.setModifyMan(entity.getModifyMan());
        }
        return dto;
    }
}
