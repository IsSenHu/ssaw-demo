package com.ssaw.ssawuserresourceservice.transfer;

import com.ssaw.ssawuserresourcefeign.dto.ScopeDto;
import com.ssaw.ssawuserresourceservice.entity.ScopeEntity;
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
