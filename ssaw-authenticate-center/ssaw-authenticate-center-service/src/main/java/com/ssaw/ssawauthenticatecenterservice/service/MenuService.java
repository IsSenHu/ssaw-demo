package com.ssaw.ssawauthenticatecenterservice.service;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.dto.Button;
import com.ssaw.ssawauthenticatecenterfeign.dto.Menu;

import java.util.List;
import java.util.Set;

/**
 * @author HuSen
 * @date 2019/2/26 10:35
 */
public interface MenuService {

    /**
     * 上传菜单
     * @param menu 菜单
     * @param resourceId 资源ID
     * @return 上传结果
     */
    CommonResult<String> uploadMenus(Menu menu, String resourceId);


    /**
     * 获取菜单
     * @param scope 作用域
     * @param resourceIds 资源ID
     * @return 菜单
     */
    List<Menu> getMenus(Set<String> scope, Set<String> resourceIds);

    /**
     * 上传按钮
     * @param buttons 按钮
     * @param resourceId 资源ID
     * @return 上传结果
     */
    CommonResult<String> uploadButtons(List<Button> buttons, String resourceId);

    /**
     * 获取按钮
     * @param scope 作用域
     * @param resourceIds 资源ID
     * @return 按钮
     */
    List<Button> getButtons(Set<String> scope, Set<String> resourceIds);

    /**
     * 上传白名单
     * @param whiteList 白名单
     * @param resourceId 资源ID
     * @return 上传结果
     */
    CommonResult<String> uploadWhiteList(List<String> whiteList, String resourceId);
}
