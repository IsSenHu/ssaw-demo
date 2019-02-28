package com.ssaw.ssawauthenticatecenterservice.transfer;

import com.ssaw.ssawauthenticatecenterfeign.vo.RoleDto;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.role.RoleEntity;
import org.springframework.stereotype.Component;

/**
 * @author HuSen.
 * @date 2018/12/14 19:48.
 */
@Component
public class RoleTransfer {

    public RoleEntity dto2Entity(RoleDto dto) {
        RoleEntity entity = null;
        if(null != dto) {
            entity = new RoleEntity();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
        }
        return entity;
    }

    public RoleDto entity2Dto(RoleEntity entity) {
        RoleDto dto = null;
        if(null != entity) {
            dto = new RoleDto();
            dto.setId(entity.getId());
            dto.setName(entity.getName());
            dto.setDescription(entity.getDescription());
            dto.setCreateMan(entity.getCreateMan());
            dto.setCreateTime(entity.getCreateTime());
            dto.setModifyMan(entity.getModifyMan());
            dto.setModifyTime(entity.getModifyTime());
        }
        return dto;
    }
}
