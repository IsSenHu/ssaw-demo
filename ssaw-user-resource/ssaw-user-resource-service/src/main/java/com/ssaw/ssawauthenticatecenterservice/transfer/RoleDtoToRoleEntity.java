package com.ssaw.ssawauthenticatecenterservice.transfer;

import com.ssaw.ssawuserresourcefeign.dto.RoleDto;
import com.ssaw.ssawauthenticatecenterservice.entity.RoleEntity;
import org.springframework.stereotype.Component;
import java.util.function.Function;

/**
 * @author HuSen.
 * @date 2018/12/3 15:49.
 */
@Component
public class RoleDtoToRoleEntity implements Function<RoleDto, RoleEntity> {
    @Override
    public RoleEntity apply(RoleDto roleDto) {
        RoleEntity roleEntity = null;
        if(null != roleDto) {
            roleEntity = new RoleEntity();
            roleEntity.setCreateTime(roleDto.getCreateTime());
            roleEntity.setDescription(roleDto.getDescription());
            roleEntity.setId(roleDto.getId());
            roleEntity.setIsParent(roleDto.getIsParent());
            roleEntity.setParentId(roleDto.getParentId());
            roleEntity.setType(roleDto.getType());
            roleEntity.setUniqueMark(roleDto.getUniqueMark());
            roleEntity.setUpdateTime(roleDto.getUpdateTime());
            roleEntity.setUrl(roleDto.getUrl());
        }
        return roleEntity;
    }
}
