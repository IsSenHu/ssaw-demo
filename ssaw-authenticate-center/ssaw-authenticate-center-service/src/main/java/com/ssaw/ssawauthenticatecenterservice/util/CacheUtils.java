package com.ssaw.ssawauthenticatecenterservice.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.ssawauthenticatecenterfeign.dto.Button;
import com.ssaw.ssawauthenticatecenterfeign.dto.Menu;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author HuSen.
 * @date 2019/1/23 13:13.
 */
@Slf4j
public class CacheUtils {
    private static final Cache<String, List<ScopeDto>> SCOPE_CACHE = CacheBuilder.newBuilder().build();
    private static final Cache<String, Menu> MENU_CACHE = CacheBuilder.newBuilder().build();
    private static final Cache<String, List<Button>> BUTTON_CACHE = CacheBuilder.newBuilder().build();
    private static final Cache<String, List<String>> WHITE_LIST_CACHE = CacheBuilder.newBuilder().build();

    public synchronized static void refreshScopes(String resourceId, List<ScopeDto> scopes) {
        SCOPE_CACHE.put(resourceId, scopes);
    }

    public synchronized static void addButtons(String resourceId, List<Button> buttons) {
        log.info("新增按钮:{}->{}", resourceId, JsonUtils.object2JsonString(buttons));
        BUTTON_CACHE.put(resourceId, buttons);
    }

    public synchronized static void addMenus(String resourceId, Menu menu) {
        log.info("新增菜单:{}->{}", resourceId, JsonUtils.object2JsonString(menu));
        MENU_CACHE.put(resourceId, menu);
    }

    public synchronized static void refreshWhiteList(String resourceId, List<String> whiteList) {
        log.info("新增白名单:{}->{}", resourceId, whiteList);
        WHITE_LIST_CACHE.put(resourceId, whiteList);
    }

    public static List<String> getWhiteList() {
        ConcurrentMap<String, List<String>> concurrentMap = WHITE_LIST_CACHE.asMap();
        List<String> whiteList = new ArrayList<>();
        concurrentMap.values().forEach(whiteList::addAll);
        return whiteList;
    }

    public static List<ScopeDto> getScopes() {
        ConcurrentMap<String, List<ScopeDto>> concurrentMap = SCOPE_CACHE.asMap();
        List<ScopeDto> scopeDtoList = new ArrayList<>();
        concurrentMap.values().forEach(scopeDtoList::addAll);
        return scopeDtoList;
    }

    public static Menu getMenu(String resourceId) {
        return MENU_CACHE.getIfPresent(resourceId);
    }

    public static List<Button> getButtons(String resourceId) {
        return BUTTON_CACHE.getIfPresent(resourceId);
    }
}
