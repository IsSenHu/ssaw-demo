package com.ssaw.ssawauthenticatecenterservice.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterservice.config.OAuth2Config;

import java.util.List;

/**
 * @author HuSen.
 * @date 2019/1/23 13:13.
 */
public class CacheUtils {
    private static final Cache<String, List<ScopeDto>> SCOPE_CACHE = CacheBuilder.newBuilder().build();

    public static void refreshScopes(List<ScopeDto> dtos) {
        SCOPE_CACHE.cleanUp();
        SCOPE_CACHE.put(OAuth2Config.SCOPE_CACHE_KEY, dtos);
    }

    public static List<ScopeDto> getScopes() {
        return SCOPE_CACHE.getIfPresent(OAuth2Config.SCOPE_CACHE_KEY);
    }
}
