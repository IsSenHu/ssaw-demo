package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.dto.ClientDto;
import com.ssaw.ssawauthenticatecenterservice.entity.ClientDetailsEntity;
import com.ssaw.ssawauthenticatecenterservice.repository.client.ClientRepository;
import com.ssaw.ssawauthenticatecenterservice.service.ClientService;
import com.ssaw.ssawauthenticatecenterservice.transfer.ClientTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
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
        return optionalDetailsEntity.get();
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
