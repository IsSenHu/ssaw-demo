package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.ssawauthenticatecenterfeign.dto.AuthenticationDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.AuthenticationResultDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.service.RemoteTokenService;
import com.ssaw.ssawauthenticatecenterservice.util.CacheUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @author HuSen.
 * @date 2019/1/23 10:05.
 */
@Slf4j
@Service
public class RemoteTokenServiceImpl implements RemoteTokenService {
    private final RedisTokenStore redisTokenStore;
    private final ClientDetailsService clientDetailsService;
    private AntPathMatcher antPathMatcher;

    @PostConstruct
    private void init() {
        antPathMatcher = new AntPathMatcher(File.separator);
    }

    @Autowired
    public RemoteTokenServiceImpl(RedisTokenStore redisTokenStore, ClientDetailsService clientDetailsService) {
        this.redisTokenStore = redisTokenStore;
        this.clientDetailsService = clientDetailsService;
    }

    /**
     * 验证token
     * @param authentication 认证信息
     * @return 认证信息
     */
    @Override
    public AuthenticationResultDto authenticate(AuthenticationDto authentication) {
        if(authentication == null || StringUtils.isBlank(authentication.getTokenValue()) || StringUtils.isBlank(authentication.getUri())) {
            return AuthenticationResultDto.fail("Authentication is empty: " + authentication);
        }
        OAuth2AccessToken token = redisTokenStore.readAccessToken(authentication.getTokenValue());
        if(null == token) {
            return AuthenticationResultDto.fail("Invalid access token: " + authentication.getTokenValue());
        } else if(token.isExpired()) {
            redisTokenStore.removeAccessToken(authentication.getTokenValue());
            return AuthenticationResultDto.fail("Token is expired: " + authentication.getTokenValue());
        }
        OAuth2Authentication result = redisTokenStore.readAuthentication(token);
        if(result == null) {
            return AuthenticationResultDto.fail("Invalid access token: " + authentication.getTokenValue());
        }
        ClientDetails clientDetails = null;
        String clientId = result.getOAuth2Request().getClientId();
        if(null != clientDetailsService) {
            try {
                clientDetails = clientDetailsService.loadClientByClientId(clientId);
            } catch (ClientRegistrationException e) {
                return AuthenticationResultDto.fail(e.getMessage());
            }
        }
        Set<String> resourceIds = result.getOAuth2Request().getResourceIds();
        String resourceId = getResourceId(authentication.getUri());
        if(StringUtils.isNotBlank(resourceId) && CollectionUtils.isNotEmpty(resourceIds) && !resourceIds.contains(resourceId)) {
            return AuthenticationResultDto.fail("Invalid token does not contain resource id (" + resourceId + ")");
        }
        if(null == clientDetails) {
            return AuthenticationResultDto.fail("Not found client for clientId: " + clientId);
        }
        Set<String> allowed = clientDetails.getScope();
        for (String scope : result.getOAuth2Request().getScope()) {
            if(!allowed.contains(scope)) {
                return AuthenticationResultDto.fail("Invalid token contains disallowed scope (" + scope + ") for this client");
            }
        }
        return AuthenticationResultDto.success();
    }

    private String getResourceId(String uri) {
        List<ScopeDto> scopes = CacheUtils.getScopes();
        if(CollectionUtils.isNotEmpty(scopes)) {
            for(ScopeDto scope : scopes) {
                if(antPathMatcher.match(scope.getUri(), uri)) {
                    return scope.getResourceName();
                }
            }
        }
        return null;
    }
}
