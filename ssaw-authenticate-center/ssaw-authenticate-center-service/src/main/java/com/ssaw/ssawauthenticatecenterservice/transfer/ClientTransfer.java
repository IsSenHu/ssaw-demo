package com.ssaw.ssawauthenticatecenterservice.transfer;

import com.ssaw.ssawauthenticatecenterfeign.dto.ClientDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ClientDetailsEntity;
import org.springframework.stereotype.Component;

/**
 * @author HuSen.
 * @date 2019/1/4 13:48.
 */
@Component
public class ClientTransfer {

    public ClientDetailsEntity dto2Entity(ClientDto dto) {
        ClientDetailsEntity entity = null;
        if(null != dto) {
            entity = new ClientDetailsEntity();
            entity.setClientId(dto.getClientId());
            entity.setAccessTokenValiditySeconds(dto.getAccessTokenValiditySeconds());
            entity.setAuthorizedGrantTypes(dto.getAuthorizedGrantTypes());
            entity.setClientSecret(dto.getClientSecret());
            entity.setCreateMan(dto.getCreateMan());
            entity.setCreateTime(dto.getCreateTime());
            entity.setRefreshTokenValiditySeconds(dto.getRefreshTokenValiditySeconds());
            entity.setRegisteredRedirectUris(dto.getRegisteredRedirectUris());
            entity.setResourceIds(dto.getResourceIds());
            entity.setScopes(dto.getScopes());
            entity.setUpdateMan(dto.getUpdateMan());
            entity.setUpdateTime(dto.getUpdateTime());
            entity.setUserId(dto.getUserId());
        }
        return entity;
    }
}
