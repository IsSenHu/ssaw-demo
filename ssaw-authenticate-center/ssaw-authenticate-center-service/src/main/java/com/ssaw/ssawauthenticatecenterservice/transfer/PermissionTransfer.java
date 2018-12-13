package com.ssaw.ssawauthenticatecenterservice.transfer;

import com.ssaw.ssawauthenticatecenterfeign.dto.PermissionDto;
import com.ssaw.ssawauthenticatecenterservice.entity.PermissionEntity;
import org.springframework.stereotype.Component;

/**
 * @author HuSen.
 * @date 2018/12/13 17:10.
 */
@Component
public class PermissionTransfer {

    public PermissionEntity dto2Entity(PermissionDto dto) {
        PermissionEntity entity = null;
        if(null != dto) {
            entity = new PermissionEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setResourceId(dto.getResourceId());
            entity.setScopeId(dto.getScopeId());
        }
        return entity;
    }

}
