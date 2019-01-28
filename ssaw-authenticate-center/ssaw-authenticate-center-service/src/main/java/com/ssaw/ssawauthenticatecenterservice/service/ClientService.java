package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.commons.vo.PageReqDto;
import com.ssaw.commons.vo.TableData;
import com.ssaw.ssawauthenticatecenterfeign.dto.ClientDetailsInfoDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.ClientDto;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * @author HuSen.
 * @date 2018/12/11 9:32.
 */
public interface ClientService extends ClientDetailsService {

    CommonResult<ClientDetailsInfoDto> findById(String clientId);

    CommonResult<ClientDto> save(ClientDto clientDto);

    TableData<ClientDto> page(PageReqDto<ClientDto> pageReq);

    CommonResult<String> delete(String id);
}
