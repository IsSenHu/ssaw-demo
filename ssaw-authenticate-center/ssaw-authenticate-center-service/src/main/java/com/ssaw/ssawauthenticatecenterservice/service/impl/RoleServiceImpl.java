package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.PermissionDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.RoleDto;
import com.ssaw.ssawauthenticatecenterservice.dto.RolePermissionReqDto;
import com.ssaw.ssawauthenticatecenterservice.entity.RoleEntity;
import com.ssaw.ssawauthenticatecenterservice.entity.RolePermissionEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.permission.PermissionRepository;
import com.ssaw.ssawauthenticatecenterservice.repository.role.RoleRepository;
import com.ssaw.ssawauthenticatecenterservice.repository.role.permission.RolePermissionRepository;
import com.ssaw.ssawauthenticatecenterservice.service.RoleService;
import com.ssaw.ssawauthenticatecenterservice.specification.RoleSpecification;
import com.ssaw.ssawauthenticatecenterservice.transfer.PermissionTransfer;
import com.ssaw.ssawauthenticatecenterservice.transfer.RoleTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.ssaw.commons.constant.Constants.ResultCodes.DATA_EXIST;
import static com.ssaw.commons.constant.Constants.ResultCodes.DATA_NOT_EXIST;
import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;

/**
 * @author HuSen.
 * @date 2018/12/14 17:53.
 */
@Slf4j
@Service
public class RoleServiceImpl extends BaseService implements RoleService {

    private final RoleTransfer roleTransfer;

    private final RoleRepository roleRepository;

    private final RolePermissionRepository rolePermissionRepository;

    private final PermissionTransfer permissionTransfer;

    private final PermissionRepository permissionRepository;

    @Autowired
    public RoleServiceImpl(RoleTransfer roleTransfer, RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository, PermissionTransfer permissionTransfer, PermissionRepository permissionRepository) {
        this.roleTransfer = roleTransfer;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionTransfer = permissionTransfer;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<RoleDto> add(RoleDto roleDto) {
        if(roleRepository.countByName(roleDto.getName()) > 0) {
            return CommonResult.createResult(DATA_EXIST, "该权限名称已存在!", roleDto);
        }
        RoleEntity entity = roleTransfer.dto2Entity(roleDto);
        entity.setCreateTime(LocalDateTime.now());
        roleRepository.save(entity);
        return CommonResult.createResult(SUCCESS, "成功!", roleDto);
    }

    @Override
    public CommonResult<RoleDto> findById(Long id) {
        return roleRepository.findById(id)
                .map(entity -> CommonResult.createResult(SUCCESS, "成功!", roleTransfer.entity2Dto(entity)))
                .orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "该角色不存在!", null));
    }

    @Override
    public TableData<RoleDto> page(PageReqDto<RoleDto> pageReqDto) {
        Pageable pageable = getPageRequest(pageReqDto);
        Page<RoleEntity> page = roleRepository.findAll(new RoleSpecification(pageReqDto.getData()), pageable);
        TableData<RoleDto> tableData = new TableData<>();
        setTableData(page, tableData);
        tableData.setContent(page.getContent().stream().map(roleTransfer::entity2Dto).collect(Collectors.toList()));
        return tableData;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<RoleDto> update(RoleDto roleDto) {
        return roleRepository.findById(roleDto.getId())
            .map(entity -> {
                if(!StringUtils.equals(entity.getName(), roleDto.getName()) && roleRepository.countByName(roleDto.getName()) > 0) {
                    return CommonResult.createResult(DATA_EXIST, "该角色名称已存在!", roleDto);
                }
                entity.setName(roleDto.getName());
                entity.setDescription(roleDto.getDescription());
                entity.setModifyTime(LocalDateTime.now());
                roleRepository.save(entity);
                return CommonResult.createResult(SUCCESS, "成功!", roleDto);
            })
            .orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "该角色不存在!", roleDto));
    }

    @Override
    public CommonResult<List<PermissionDto>> findAllPermissionByRoleId(Long id) {
        Set<Long> permissionIdSet = rolePermissionRepository.findAllByRoleId(id)
                .stream().map(RolePermissionEntity::getPermissionId).collect(Collectors.toSet());

        List<PermissionDto> permissionDtoList;
        if(CollectionUtils.isNotEmpty(permissionIdSet)) {
            permissionDtoList = permissionRepository.findAllById(permissionIdSet)
                    .stream().map(permissionTransfer::simpleEntity2Dto).collect(Collectors.toList());
        } else {
            permissionDtoList = new ArrayList<>(0);
        }
        return CommonResult.createResult(SUCCESS, "成功!", permissionDtoList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<Long> changeRolePermission(RolePermissionReqDto reqDto) {
        rolePermissionRepository.deleteAllByRoleId(reqDto.getRoleId());
        String permissionIds = reqDto.getPermissionIds();
        if(StringUtils.isNotBlank(permissionIds)) {
            String[] split = permissionIds.split(",");
            List<RolePermissionEntity> rps = Arrays.stream(split).filter(NumberUtils::isParsable).map(id -> {
                RolePermissionEntity entity = new RolePermissionEntity();
                entity.setRoleId(reqDto.getRoleId());
                entity.setPermissionId(Long.parseLong(id));
                return entity;
            }).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(rps)) {
                rolePermissionRepository.saveAll(rps);
            }
        }
        return CommonResult.createResult(SUCCESS, "成功!", reqDto.getRoleId());
    }

    @Override
    public CommonResult<Long> delete(Long id) {
        roleRepository.deleteById(id);
        return CommonResult.createResult(SUCCESS, "成功!", id);
    }
}
