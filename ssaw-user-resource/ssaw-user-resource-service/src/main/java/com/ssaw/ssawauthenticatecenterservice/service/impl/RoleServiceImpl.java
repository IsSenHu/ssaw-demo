package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.google.common.collect.Lists;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawuserresourcefeign.dto.RoleDto;
import com.ssaw.ssawauthenticatecenterservice.entity.RoleEntity;
import com.ssaw.ssawauthenticatecenterservice.entity.UserRoleEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.RoleRepository;
import com.ssaw.ssawauthenticatecenterservice.repository.UserRoleRepository;
import com.ssaw.ssawauthenticatecenterservice.service.BaseService;
import com.ssaw.ssawauthenticatecenterservice.service.RoleService;
import com.ssaw.ssawauthenticatecenterservice.transfer.RoleDtoToRoleEntity;
import com.ssaw.ssawauthenticatecenterservice.transfer.RoleEntityToRoleDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.ssaw.commons.constant.Constants.ResultCodes.*;

/**
 * @author HuSen.
 * @date 2018/11/27 19:40.
 */
@Slf4j
@Service
public class RoleServiceImpl extends BaseService implements RoleService {
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleEntityToRoleDto roleEntityToRoleDto;
    private final RoleDtoToRoleEntity roleDtoToRoleEntity;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, RoleEntityToRoleDto roleEntityToRoleDto, RoleDtoToRoleEntity roleDtoToRoleEntity, UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.roleEntityToRoleDto = roleEntityToRoleDto;
        this.roleDtoToRoleEntity = roleDtoToRoleEntity;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public CommonResult<List<RoleDto>> findAllRoleByUserId(Long userId) {
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findAllByUserId(userId);
        List<Long> collect = userRoleEntities.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
        List<RoleEntity> roleEntities = roleRepository.findAllByIdIn(collect);
        if(CollectionUtils.isEmpty(roleEntities)) {
            return createResult(DATA_NOT_EXIST, "没有查询到相关的角色!", null);
        }
        return createResult(SUCCESS, "成功!", Lists.transform(roleEntities, roleEntityToRoleDto));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<RoleDto> save(RoleDto roleDto) {
        if(Objects.isNull(roleDto.getId())) {
            RoleEntity roleEntity = roleDtoToRoleEntity.apply(roleDto);
            roleDto.setCreateTime(roleEntity.getCreateTime());
            roleRepository.save(roleEntity);
        } else {
            Optional<RoleEntity> byId = roleRepository.findById(roleDto.getId());
            if(!byId.isPresent()) {
                return createResult(DATA_NOT_EXIST, "该角色不存在!", roleDto);
            }
            RoleEntity oldRole = byId.get();
            if(!StringUtils.equals(oldRole.getUniqueMark(), roleDto.getUniqueMark())) {
                if(roleRepository.countByUniqueMark(roleDto.getUniqueMark()) != 0) {
                    return createResult(DATA_EXIST, "该角色标识已存在!", roleDto);
                }
            }
            BeanUtils.copyProperties(roleDto, oldRole);
            roleRepository.save(oldRole);
            roleDto.setCreateTime(oldRole.getCreateTime());
            roleDto.setUpdateTime(oldRole.getUpdateTime());
        }
        return createResult(SUCCESS, "成功!", roleDto);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<Long> delete(Long roleId) {
        if(Objects.isNull(roleId)) {
            return createResult(PARAM_ERROR, "角色ID不能为空!", null);
        }
        roleRepository.deleteById(roleId);
        userRoleRepository.deleteAllByRoleId(roleId);
        return createResult(SUCCESS, "成功!", roleId);
    }
}
