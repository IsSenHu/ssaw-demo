package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.dto.ClientDto;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * @author HuSen.
 * @date 2018/12/11 9:32.
 */
public interface ClientService extends ClientDetailsService {

    CommonResult<ClientDto> findById(String clientId);

    CommonResult<ClientDto> save(ClientDto clientDto);
}
