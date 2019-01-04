package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.netflix.discovery.converters.Auto;
import com.ssaw.commons.security.SecurityUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.dto.ClientDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ClientDetailsEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.client.ClientRepository;
import com.ssaw.ssawauthenticatecenterservice.service.ClientService;
import com.ssaw.ssawauthenticatecenterservice.transfer.ClientTransfer;
import com.ssaw.ssawauthenticatecenterservice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

/**
 * @author HuSen.
 * @date 2018/12/11 9:33.
 */
@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientTransfer clientTransfer;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, ClientTransfer clientTransfer) {
        this.clientRepository = clientRepository;
        this.clientTransfer = clientTransfer;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        Optional<ClientDetailsEntity> optionalDetailsEntity = clientRepository.findById(clientId);
        if(!optionalDetailsEntity.isPresent()) {
            log.error("客户端:{}, 不存在", clientId);
            throw new ClientRegistrationException("客户端不存在!");
        }
        ClientDetailsEntity clientDetailsEntity = optionalDetailsEntity.get();
        UserVo userVo = SecurityUtils.getUserDetails(UserVo.class);
        if(Objects.isNull(userVo)) {
            log.error("用户:{}, 未登录!", clientDetailsEntity.getUserId());
            throw new ClientRegistrationException("用户未登录!");
        }
        if(NumberUtils.compare(userVo.getId(), clientDetailsEntity.getUserId()) != 0) {
            log.error("帐号:{}, 与客户端:{}, 不匹配!", userVo.getUsername(), clientId);
            throw new ClientRegistrationException("帐号与客户端不匹配!");
        }
        return clientDetailsEntity;
    }

    @Override
    public CommonResult<ClientDto> findById(String clientId) {
        return null;
    }

    @Override
    public CommonResult<ClientDto> save(ClientDto clientDto) {

        return null;
    }
}
