package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ScopeEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.scope.ScopeRepository;
import com.ssaw.ssawauthenticatecenterservice.service.ScopeService;
import com.ssaw.ssawauthenticatecenterservice.specification.ScopeSpecification;
import com.ssaw.ssawauthenticatecenterservice.transfer.ScopeTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
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

    @Autowired
    public ScopeServiceImpl(ScopeRepository scopeRepository, ScopeTransfer scopeTransfer) {
        this.scopeRepository = scopeRepository;
        this.scopeTransfer = scopeTransfer;
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
        PageRequest pageRequest = getPageRequest(pageReqDto);
        Page<ScopeEntity> page = scopeRepository.findAll(new ScopeSpecification(pageReqDto.getData()), pageRequest);
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
}
