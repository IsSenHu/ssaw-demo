package com.ssaw.ssawauthenticatecenterservice.transfer;

import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ScopeEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author HuSen.
 * @date 2018/12/12 11:52.
 */
@Component
public class ScopeDtoToScopeEntity implements Function<ScopeDto, ScopeEntity> {
    @Override
    public ScopeEntity apply(ScopeDto scopeDto) {
        ScopeEntity entity = null;
        if(null != scopeDto) {
            entity = new ScopeEntity();
            BeanUtils.copyProperties(scopeDto, entity);
        }
        return entity;
    }
}
