package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.vo.PermissionDto;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.permission.PermissionEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.scope.ScopeEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.permission.PermissionRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.scope.ScopeRepository;
import com.ssaw.ssawauthenticatecenterservice.service.PermissionService;
import com.ssaw.ssawauthenticatecenterservice.specification.PermissionSpecification;
import com.ssaw.ssawauthenticatecenterservice.transfer.PermissionTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * 新增权限
     * @param permissionDto 新增权限请求对象
     * @return 新增结果
     */
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
        Optional<ScopeEntity> optionalScopeEntity = scopeRepository.findById(permissionDto.getScopeId());
        optionalScopeEntity.ifPresent(scope -> entity.setResourceId(scope.getResourceId()));
        entity.setCreateTime(LocalDateTime.now());
        PermissionEntity save = permissionRepository.save(entity);
        optionalScopeEntity.ifPresent(scope -> {
            scope.setPermissionId(save.getId());
            scopeRepository.save(scope);
        });
        return CommonResult.createResult(SUCCESS, "成功!", permissionDto);
    }

    /**
     * 分页查询权限
     * @param pageReqDto 分页查询参数
     * @return 分页结果
     */
    @Override
    public TableData<PermissionDto> page(PageReqDto<PermissionDto> pageReqDto) {
        Pageable pageable = getPageRequest(pageReqDto);
        Page<PermissionEntity> page = permissionRepository.findAll(new PermissionSpecification(pageReqDto.getData()), pageable);
        TableData<PermissionDto> tableData = new TableData<>();
        setTableData(page, tableData);
        tableData.setContent(page.getContent().stream().map(permissionTransfer::entity2Dto).collect(Collectors.toList()));
        return tableData;
    }

    /**
     * 根据ID删除权限
     * @param id ID
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<Long> delete(Long id) {
        permissionRepository.deleteById(id);
        return CommonResult.createResult(SUCCESS, "成功!", id);
    }

    /**
     * 修改权限
     * @param permissionDto 修改请求对象
     * @return 修改结果
     */
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
            if(!Objects.isNull(permissionDto.getScopeId()) && !Objects.equals(permissionDto.getScopeId(), entity.getScopeId())) {
                entity.setScopeId(permissionDto.getScopeId());
                scopeRepository.findById(entity.getScopeId())
                        .ifPresent(scope -> {
                            scope.setPermissionId(null);
                            scopeRepository.save(scope);
                        });
                scopeRepository.findById(permissionDto.getScopeId())
                        .ifPresent(scope -> {
                            entity.setResourceId(scope.getResourceId());
                            scope.setPermissionId(entity.getId());
                            scopeRepository.save(scope);
                        });
            }
            entity.setModifyTime(LocalDateTime.now());
            permissionRepository.save(entity);
            return CommonResult.createResult(SUCCESS, "成功!", permissionDto);
        }).orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "该权限不存在!", permissionDto));
    }

    /**
     * 根据ID查询权限
     * @param id ID
     * @return 权限
     */
    @Override
    public CommonResult<PermissionDto> findById(Long id) {
        return permissionRepository.findById(id)
                .map(entity -> CommonResult.createResult(SUCCESS, "成功!", permissionTransfer.entity2Dto(entity)))
                .orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "该权限不存在!", null));
    }
}
