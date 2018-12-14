package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.PermissionDto;
import com.ssaw.ssawauthenticatecenterservice.entity.PermissionEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.permission.PermissionRepository;
import com.ssaw.ssawauthenticatecenterservice.repository.scope.ScopeRepository;
import com.ssaw.ssawauthenticatecenterservice.service.PermissionService;
import com.ssaw.ssawauthenticatecenterservice.specification.PermissionSpecification;
import com.ssaw.ssawauthenticatecenterservice.transfer.PermissionTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.ssaw.commons.constant.Constants.ResultCodes.*;

/**
 * @author HuSen.
 * @date 2018/12/13 17:05.
 */
@Service
@Slf4j
public class PermissionServiceImpl extends BaseService implements PermissionService {

    private final PermissionTransfer permissionTransfer;
    private final PermissionRepository permissionRepository;
    private final ScopeRepository scopeRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionTransfer permissionTransfer, ScopeRepository scopeRepository) {
        this.permissionRepository = permissionRepository;
        this.permissionTransfer = permissionTransfer;
        this.scopeRepository = scopeRepository;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<PermissionDto> add(PermissionDto permissionDto) {
        PermissionEntity entity = permissionTransfer.dto2Entity(permissionDto);
        if(null == entity) {
            return CommonResult.createResult(PARAM_ERROR, "参数不能为空!", null);
        }
        if(permissionRepository.countByName(permissionDto.getName()) > 0) {
            return CommonResult.createResult(DATA_EXIST, "权限名称已存在!", permissionDto);
        }
        scopeRepository.findById(permissionDto.getScopeId())
                .ifPresent(scope -> entity.setResourceId(scope.getResourceId()));
        entity.setCreateTime(LocalDateTime.now());
        permissionRepository.save(entity);
        return CommonResult.createResult(SUCCESS, "成功!", permissionDto);
    }

    @Override
    public TableData<PermissionDto> page(PageReqDto<PermissionDto> pageReqDto) {
        PageRequest pageRequest = getPageRequest(pageReqDto);
        Page<PermissionEntity> page = permissionRepository.findAll(new PermissionSpecification(pageReqDto.getData()), pageRequest);
        TableData<PermissionDto> tableData = new TableData<>();
        setTableData(page, tableData);
        tableData.setContent(page.getContent().stream().map(permissionTransfer::entity2Dto).collect(Collectors.toList()));
        return tableData;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<Long> delete(Long id) {
        permissionRepository.deleteById(id);
        return CommonResult.createResult(SUCCESS, "成功!", id);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<PermissionDto> update(PermissionDto permissionDto) {
        Optional<PermissionEntity> optional = permissionRepository.findById(permissionDto.getId());
        return optional.map(entity -> {
            if(!StringUtils.equals(entity.getName(), permissionDto.getName()) && permissionRepository.countByName(permissionDto.getName()) > 0) {
                return CommonResult.createResult(DATA_EXIST, "该权限名称已存在!", permissionDto);
            }
            entity.setName(permissionDto.getName());
            entity.setDescription(permissionDto.getDescription());
            if(!Objects.equals(permissionDto.getScopeId(), entity.getScopeId())) {
                entity.setScopeId(permissionDto.getScopeId());
                scopeRepository.findById(permissionDto.getScopeId())
                        .ifPresent(scope -> entity.setResourceId(scope.getResourceId()));
            }
            entity.setModifyTime(LocalDateTime.now());
            permissionRepository.save(entity);
            return CommonResult.createResult(SUCCESS, "成功!", permissionDto);
        }).orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "该权限不存在!", permissionDto));
    }
}
