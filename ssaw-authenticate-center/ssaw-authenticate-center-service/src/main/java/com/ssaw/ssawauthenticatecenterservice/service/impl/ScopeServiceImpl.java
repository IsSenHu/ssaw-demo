package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ScopeEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.scope.ScopeRepository;
import com.ssaw.ssawauthenticatecenterservice.service.ScopeService;
import com.ssaw.ssawauthenticatecenterservice.transfer.ScopeDtoToScopeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.ssaw.commons.constant.Constants.ResultCodes.PARAM_ERROR;
import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;

/**
 * @author HuSen.
 * @date 2018/12/12 11:38.
 */
@Slf4j
@Service
public class ScopeServiceImpl implements ScopeService {

    private final ScopeDtoToScopeEntity scopeDtoToScopeEntity;
    private final ScopeRepository scopeRepository;

    @Autowired
    public ScopeServiceImpl(ScopeRepository scopeRepository, ScopeDtoToScopeEntity scopeDtoToScopeEntity) {
        this.scopeRepository = scopeRepository;
        this.scopeDtoToScopeEntity = scopeDtoToScopeEntity;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult<ScopeDto> add(ScopeDto scopeDto) {
        ScopeEntity entity = scopeDtoToScopeEntity.apply(scopeDto);
        if(null == entity) {
            return CommonResult.createResult(PARAM_ERROR, "参数为空!", null);
        }
        scopeRepository.save(entity);
        return CommonResult.createResult(SUCCESS, "成功!", scopeDto);
    }
}
