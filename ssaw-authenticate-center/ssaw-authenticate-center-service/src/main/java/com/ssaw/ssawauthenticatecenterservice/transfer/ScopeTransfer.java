package com.ssaw.ssawauthenticatecenterservice.transfer;

import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ResourceEntity;
import com.ssaw.ssawauthenticatecenterservice.entity.ScopeEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.resource.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * @author HuSen.
 * @date 2018/12/13 12:56.
 */
@Component
public class ScopeTransfer {

    private final ResourceRepository resourceRepository;

    @Autowired
    public ScopeTransfer(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public ScopeEntity dto2Entity(ScopeDto scopeDto) {
        ScopeEntity entity = null;
        if (scopeDto != null) {
            entity = new ScopeEntity();
            entity.setId(scopeDto.getId());
            entity.setUri(scopeDto.getUri());
            entity.setScope(scopeDto.getScope());
            entity.setResourceId(scopeDto.getResourceId());
        }
        return entity;
    }

    public ScopeDto entity2Dto(ScopeEntity scopeEntity) {
        ScopeDto dto = new ScopeDto();
        if(scopeEntity != null) {
            dto = new ScopeDto();
            Optional<ResourceEntity> optional = resourceRepository.findById(scopeEntity.getResourceId());
            if(optional.isPresent()) {
                dto.setResourceName(optional.get().getResourceId());
            }
            dto.setId(scopeEntity.getId());
            dto.setScope(scopeEntity.getScope());
            dto.setUri(scopeEntity.getUri());
            dto.setResourceId(scopeEntity.getResourceId());
            dto.setCreateTime(scopeEntity.getCreateTime());
            dto.setCreateMan(scopeEntity.getCreateMan());
            dto.setModifyTime(scopeEntity.getModifyTime());
            dto.setModifyMan(scopeEntity.getModifyMan());
        }
        return dto;
    }
}
