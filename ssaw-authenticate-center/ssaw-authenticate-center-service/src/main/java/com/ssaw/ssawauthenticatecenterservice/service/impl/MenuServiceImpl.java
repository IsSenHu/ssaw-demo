package com.ssaw.ssawauthenticatecenterservice.service.impl;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.dto.Button;
import com.ssaw.ssawauthenticatecenterfeign.dto.Menu;
import com.ssaw.ssawauthenticatecenterservice.service.MenuService;
import com.ssaw.ssawauthenticatecenterservice.util.CacheUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;

/**
 * @author HuSen
 * @date 2019/2/26 10:35
 */
@Service
public class MenuServiceImpl implements MenuService {

    /**
     * 上传菜单
     * @param menu 菜单
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @Override
    public CommonResult<String> uploadMenus(Menu menu, String resourceId) {
        CacheUtils.addMenus(resourceId, menu);
        return CommonResult.createResult(SUCCESS, "成功", resourceId);
    }

    /**
     * 获取菜单
     * @param scope 作用域
     * @param resourceIds 资源ID
     * @return 菜单
     */
    @Override
    public List<Menu> getMenus(Set<String> scope, Set<String> resourceIds) {
        List<Menu> result = new ArrayList<>();
        for (String resourceId : resourceIds) {
            Menu menu = CacheUtils.getMenu(resourceId);
            if (Objects.nonNull(menu)) {
                List<Menu> secondItems = menu.getItems().stream()
                        .sorted(Comparator.comparing(x -> Integer.valueOf(x.getIndex().replace("-", "")))).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(secondItems)) {
                    for (Menu secondItem : secondItems) {
                        List<Menu> collect = secondItem.getItems().stream().filter(t -> scope.contains(t.getScope()))
                                .sorted(Comparator.comparing(x -> Integer.valueOf(x.getIndex().replace("-", "")))).collect(Collectors.toList());
                        secondItem.setItems(collect);
                    }
                }
                result.add(menu);
            }
        }
        return result.stream().sorted(Comparator.comparing(menu -> Integer.valueOf(menu.getIndex().replace("-", "")))).collect(Collectors.toList());
    }

    /**
     * 上传按钮
     * @param buttons 按钮
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @Override
    public CommonResult<String> uploadButtons(List<Button> buttons, String resourceId) {
        CacheUtils.addButtons(resourceId, buttons);
        return CommonResult.createResult(SUCCESS, "成功", resourceId);
    }

    /**
     * 获取按钮
     * @param scope 作用域
     * @param resourceIds 资源ID
     * @return 按钮
     */
    @Override
    public List<Button> getButtons(Set<String> scope, Set<String> resourceIds) {
        List<Button> buttons = new ArrayList<>();
        for (String resourceId : resourceIds) {
            List<Button> buttonList = CacheUtils.getButtons(resourceId);
            if (Objects.nonNull(buttonList)) {
                List<Button> collect = buttonList.stream().filter(button -> scope.contains(button.getScope())).collect(Collectors.toList());
                buttons.addAll(collect);
            }
        }
        return buttons;
    }

    /**
     * 上传白名单
     * @param whiteList 白名单
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @Override
    public CommonResult<String> uploadWhiteList(List<String> whiteList, String resourceId) {
        CacheUtils.refreshWhiteList(resourceId, whiteList);
        return CommonResult.createResult(SUCCESS, "成功", resourceId);
    }
}