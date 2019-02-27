package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.entity.PermissionEntity;
import com.ssaw.ssawauthenticatecenterservice.entity.ScopeEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.permission.PermissionRepository;
import com.ssaw.ssawauthenticatecenterservice.repository.scope.ScopeRepository;
import com.ssaw.ssawauthenticatecenterservice.service.ScopeService;
import com.ssaw.ssawauthenticatecenterservice.specification.ScopeSpecification;
import com.ssaw.ssawauthenticatecenterservice.transfer.ScopeTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import static com.ssaw.commons.constant.Constants.ResultCodes.*;

/**
 * @author HuSen.
 * @date 2018/12/12 11:38.
 */
@Slf4j
@Service
public class ScopeServiceImpl extends BaseService implements ScopeService {

    private final ScopeTransfer scopeTransfer;
    private final ScopeRepository scopeRepository;
    private final PermissionRepository permissionRepository;

    @Autowired
    public ScopeServiceImpl(ScopeRepository scopeRepository, ScopeTransfer scopeTransfer, PermissionRepository permissionRepository) {
        this.scopeRepository = scopeRepository;
        this.scopeTransfer = scopeTransfer;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<ScopeDto> add(ScopeDto scopeDto) {
        ScopeEntity entity = scopeTransfer.dto2Entity(scopeDto);
        if(null == entity) {
            return CommonResult.createResult(PARAM_ERROR, "参数为空!", null);
        }
        if(scopeRepository.countByScopeOrUri(scopeDto.getScope(), scopeDto.getUri()) > 0) {
            return CommonResult.createResult(DATA_EXIST, "该scope或uri已存在!", scopeDto);
        }
        entity.setCreateTime(LocalDateTime.now());
        scopeRepository.save(entity);
        return CommonResult.createResult(SUCCESS, "成功!", scopeDto);
    }

    @Override
    public TableData<ScopeDto> page(PageReqDto<ScopeDto> pageReqDto) {
        Pageable pageable = getPageRequest(pageReqDto);
        Page<ScopeEntity> page = scopeRepository.findAll(new ScopeSpecification(pageReqDto.getData()), pageable);
        TableData<ScopeDto> tableData = new TableData<>();
        setTableData(page, tableData);
        tableData.setContent(page.getContent().stream().map(scopeTransfer::entity2Dto).collect(Collectors.toList()));
        return tableData;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<Long> delete(Long id) {
        if(Objects.isNull(id)) {
            return CommonResult.createResult(PARAM_ERROR, "作用域ID为空!", null);
        }
        scopeRepository.deleteById(id);
        return CommonResult.createResult(SUCCESS, "成功!", id);
    }

    @Override
    public CommonResult<ScopeDto> findById(Long id) {
        if(Objects.isNull(id)) {
            return CommonResult.createResult(PARAM_ERROR, "作用域ID为空!", null);
        }
        return scopeRepository.findById(id)
                .map(scopeEntity -> CommonResult.createResult(SUCCESS, "成功!", scopeTransfer.entity2Dto(scopeEntity)))
                .orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "该作用域不存在!", null));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<ScopeDto> update(ScopeDto scopeDto) {
        return scopeRepository.findById(scopeDto.getId())
                .map(entity -> {
                    if(!StringUtils.equals(scopeDto.getScope(), entity.getScope()) && scopeRepository.countByScope(scopeDto.getScope()) > 1) {
                        return CommonResult.createResult(DATA_EXIST, "该Scope已存在!", scopeDto);
                    }
                    if(!StringUtils.equals(scopeDto.getUri(), entity.getUri()) && scopeRepository.countByUri(scopeDto.getUri()) > 1) {
                        return CommonResult.createResult(DATA_EXIST, "该Uri已存在!", scopeDto);
                    }
                    entity.setScope(scopeDto.getScope());
                    entity.setUri(scopeDto.getUri());
                    entity.setResourceId(scopeDto.getResourceId());
                    entity.setModifyTime(LocalDateTime.now());
                    scopeRepository.save(entity);
                    return CommonResult.createResult(SUCCESS, "成功!", scopeDto);
                }).orElseGet(() -> CommonResult.createResult(DATA_NOT_EXIST, "该作用域不存在!", scopeDto));
    }

    @Override
    public CommonResult<List<ScopeDto>> search(String scope) {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<ScopeEntity> page;
        final String none = "none";
        if(StringUtils.equals(none, scope)) {
            page = scopeRepository.findAllByPermissionIdIsNull(pageRequest);
        } else {
            page = scopeRepository.findAllByScopeLikeAndPermissionIdIsNull("%".concat(scope.trim()).concat("%"), pageRequest);
        }
        return CommonResult.createResult(SUCCESS, "成功!",
                page.getContent().stream().map(scopeTransfer::entity2DtoNotGetResourceName).collect(Collectors.toList()));
    }

    @Override
    public CommonResult<List<ScopeDto>> searchForUpdate(String scope) {
        PageRequest pageRequest = PageRequest.of(0, 20);
        Page<ScopeEntity> page;
        final String none = "none";
        if(StringUtils.equals(none, scope)) {
            page = scopeRepository.findAll(pageRequest);
        } else {
            page = scopeRepository.findAllByScopeLike("%".concat(scope.trim()).concat("%"), pageRequest);
        }
        return CommonResult.createResult(SUCCESS, "成功!",
                page.getContent().stream().map(scopeTransfer::entity2DtoNotGetResourceName).collect(Collectors.toList()));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<String> uploadScopes(List<ScopeDto> scopeDtoList) {
        List<String> scopes = scopeDtoList.stream().map(ScopeDto::getScope).collect(Collectors.toList());
        // 先删除不要的作用域
        scopeRepository.deleteAllByScopeNotIn(scopes);
        // 得到还存在的权限
        List<ScopeEntity> allByScopeIn = scopeRepository.findAllByScopeIn(scopes);
        List<Long> allScopeId = allByScopeIn.stream().map(ScopeEntity::getId).collect(Collectors.toList());
        // 再删除不要的权限
        permissionRepository.deleteAllByScopeIdNotIn(allScopeId);
        Set<String> scopeSet = allByScopeIn.stream().map(ScopeEntity::getScope).collect(Collectors.toSet());
        Map<String, ScopeDto> updateScopeMap = new HashMap<>(scopeDtoList.size());
        List<ScopeDto> newScopeList = new ArrayList<>();
        for (ScopeDto scopeDto : scopeDtoList) {
            if (scopeSet.contains(scopeDto.getScope())) {
                updateScopeMap.put(scopeDto.getScope(), scopeDto);
            } else {
                newScopeList.add(scopeDto);
            }
        }

        allByScopeIn.forEach(entity -> {
            ScopeDto scopeDto = updateScopeMap.get(entity.getScope());
            entity.setUri(scopeDto.getUri());
            entity.setScope(scopeDto.getScope());
            entity.setResourceId(scopeDto.getResourceId());
            Optional<PermissionEntity> byId = permissionRepository.findById(entity.getPermissionId());
            byId.ifPresent(permissionEntity -> {
                permissionEntity.setScopeId(entity.getId());
                permissionEntity.setResourceId(entity.getResourceId());
                permissionEntity.setName(entity.getScope());
                permissionEntity.setDescription(entity.getUri());
                permissionEntity.setModifyTime(LocalDateTime.now());
                permissionRepository.save(permissionEntity);
                entity.setPermissionId(permissionEntity.getId());
            });
            entity.setModifyTime(LocalDateTime.now());
            scopeRepository.save(entity);
        });

        List<ScopeEntity> collect = newScopeList.stream().map(scopeTransfer::dto2Entity).collect(Collectors.toList());
        collect.forEach(entity -> {
            scopeRepository.save(entity);
            entity.setCreateTime(LocalDateTime.now());
            PermissionEntity permissionEntity = new PermissionEntity();
            permissionEntity.setScopeId(entity.getId());
            permissionEntity.setResourceId(entity.getResourceId());
            permissionEntity.setName(entity.getScope());
            permissionEntity.setDescription(entity.getUri());
            permissionEntity.setCreateTime(LocalDateTime.now());
            permissionRepository.save(permissionEntity);
            entity.setPermissionId(permissionEntity.getId());
            entity.setCreateTime(LocalDateTime.now());
            scopeRepository.save(entity);
        });
        return CommonResult.createResult(SUCCESS, "成功", UUID.randomUUID().toString());
    }

    @Override
    public void refreshScope(String source) {
        log.info("因为资源服务:{} 上传作用域, 所以开始刷新作用域", source);

    }
}
