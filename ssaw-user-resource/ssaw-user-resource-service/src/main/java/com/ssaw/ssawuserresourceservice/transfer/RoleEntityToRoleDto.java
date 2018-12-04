package com.ssaw.ssawuserresourceservice.transfer;

import com.google.common.base.Function;
import com.ssaw.ssawuserresourcefeign.dto.RoleDto;
import com.ssaw.ssawuserresourceservice.entity.RoleEntity;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author HuSen.
 * @date 2018/12/3 15:27.
 */
@Component
public class RoleEntityToRoleDto implements Function<RoleEntity, RoleDto> {

    @Override
    public RoleDto apply(@Nullable RoleEntity roleEntity) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(roleEntity.getId());
        roleDto.setCreateTime(roleEntity.getCreateTime());
        roleDto.setDescription(roleEntity.getDescription());
        roleDto.setIsParent(roleEntity.getIsParent());
        roleDto.setParentId(roleEntity.getParentId());
        roleDto.setType(roleEntity.getType());
        roleDto.setUniqueMark(roleEntity.getUniqueMark());
        roleDto.setUpdateTime(roleEntity.getUpdateTime());
        roleDto.setUrl(roleEntity.getUrl());
        return roleDto;
    }
}
