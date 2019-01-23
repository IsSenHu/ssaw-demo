package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.ssawauthenticatecenterfeign.dto.AuthenticationDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.AuthenticationResultDto;

/**
 * @author HuSen.
 * @date 2019/1/23 10:04.
 */
public interface RemoteTokenService {

    /**
     * 验证token
     * @param authentication 认证信息
     * @return 认证信息
     */
    AuthenticationResultDto authenticate(AuthenticationDto authentication);
}
