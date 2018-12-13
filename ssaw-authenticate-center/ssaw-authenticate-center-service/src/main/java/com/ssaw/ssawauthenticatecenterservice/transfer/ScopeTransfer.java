package com.ssaw.ssawauthenticatecenterservice.transfer;

import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ScopeEntity;
import org.springframework.stereotype.Component;

/**
 * @author HuSen.
 * @date 2018/12/13 12:56.
 */
@Component
public class ScopeTransfer {

    public ScopeEntity dto2Entity(ScopeDto scopeDto) {
        ScopeEntity entity = null;
        if (scopeDto != null) {
            entity = new ScopeEntity();
            entity.setId(scopeDto.getId());
            entity.setUri(scopeDto.getUri());
            entity.setScope(scopeDto.getScope());
            entity.setResourceId(scopeDto.getResourceId());
        }
        return entity;
    }
}
