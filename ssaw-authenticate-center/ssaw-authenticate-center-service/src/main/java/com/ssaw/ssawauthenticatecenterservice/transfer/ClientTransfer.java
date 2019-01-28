package com.ssaw.ssawauthenticatecenterservice.transfer;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.dto.ClientDetailsInfoDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.ClientDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ClientDetailsEntity;
import com.ssaw.ssawuserresourcefeign.dto.UserDto;
import com.ssaw.ssawuserresourcefeign.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;
import static org.apache.commons.lang.StringUtils.join;

/**
 * @author HuSen.
 * @date 2019/1/4 13:48.
 */
@Component
public class ClientTransfer {

    private final UserFeign userFeign;

    @Autowired
    public ClientTransfer(UserFeign userFeign) {
        this.userFeign = userFeign;
    }

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

    public ClientDto entity2Dto(ClientDetailsEntity entity) {
        ClientDto clientDto = null;
        if(null != entity) {
            clientDto = new ClientDto();
            clientDto.setClientId(entity.getClientId());
            clientDto.setUserId(entity.getUserId());
            clientDto.setClientSecret(entity.getClientSecret());
            clientDto.setResourceIds(join(entity.getResourceIds().toArray(new String[0])));
            clientDto.setScopes(join(entity.getScope().toArray(new String[0])));
            clientDto.setAuthorizedGrantTypes(join(entity.getAuthorizedGrantTypes().toArray(new String[0])));
            clientDto.setRegisteredRedirectUris(join(entity.getRegisteredRedirectUri().toArray(new String[0])));
            clientDto.setAccessTokenValiditySeconds(entity.getAccessTokenValiditySeconds());
            clientDto.setRefreshTokenValiditySeconds(entity.getRefreshTokenValiditySeconds());
            clientDto.setCreateTime(entity.getCreateTime());
            clientDto.setUpdateTime(entity.getUpdateTime());
            clientDto.setCreateMan(entity.getCreateMan());
            clientDto.setUpdateMan(entity.getUpdateMan());
        }
        return clientDto;
    }

    public ClientDetailsInfoDto entity2DetailsInfoDto(ClientDetailsEntity entity) {
        ClientDetailsInfoDto infoDto = null;
        if(null != entity) {
            infoDto = new ClientDetailsInfoDto();
            infoDto.setClientId(entity.getClientId());
            infoDto.setUserId(entity.getUserId());
            CommonResult<UserDto> byId = userFeign.findById(entity.getUserId());
            if(byId.getCode() == SUCCESS) {
                infoDto.setUsername(byId.getData().getUsername());
            }
            infoDto.setClientSecret(entity.getClientSecret());
            infoDto.setResourceIds(join(entity.getResourceIds().toArray(new String[0])));
            infoDto.setScopes(join(entity.getScope().toArray(new String[0])));
            infoDto.setAuthorizedGrantTypes(join(entity.getAuthorizedGrantTypes().toArray(new String[0])));
            infoDto.setRegisteredRedirectUris(join(entity.getRegisteredRedirectUri().toArray(new String[0])));
            infoDto.setAccessTokenValiditySeconds(entity.getAccessTokenValiditySeconds());
            infoDto.setRefreshTokenValiditySeconds(entity.getRefreshTokenValiditySeconds());
            infoDto.setCreateTime(entity.getCreateTime());
            infoDto.setUpdateTime(entity.getUpdateTime());
            infoDto.setCreateMan(entity.getCreateMan());
            infoDto.setUpdateMan(entity.getUpdateMan());
        }
        return infoDto;
    }
}
