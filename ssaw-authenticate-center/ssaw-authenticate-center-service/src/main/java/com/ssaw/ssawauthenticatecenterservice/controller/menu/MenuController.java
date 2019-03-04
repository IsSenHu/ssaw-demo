package com.ssaw.ssawauthenticatecenterservice.controller.menu;

import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.vo.ButtonVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.MenuVO;
import com.ssaw.ssawauthenticatecenterservice.controller.BaseController;
import com.ssaw.ssawauthenticatecenterservice.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HuSen
 * @date 2019/2/26 10:33
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController extends BaseController {

    private final MenuService menuService;

    @Autowired
    public MenuController(ApplicationContext context, MenuService menuService) {
        super(context);
        this.menuService = menuService;
    }

    /**
     * 上传菜单
     * @param menuVO 菜单
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @PostMapping("/uploadMenus/{resourceId}")
    public CommonResult<String> uploadMenus(@RequestBody MenuVO menuVO, @PathVariable(name = "resourceId") String resourceId) {
        return menuService.uploadMenus(menuVO, resourceId);
    }

    /**
     * 上传按钮
     * @param buttonVOS 按钮
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @PostMapping("/uploadButtons/{resourceId}")
    public CommonResult<String> uploadButtons(@RequestBody List<ButtonVO> buttonVOS, @PathVariable(name = "resourceId") String resourceId) {
        return menuService.uploadButtons(buttonVOS, resourceId);
    }

    /**
     * 上传白名单
     * @param whiteList 白名单
     * @param resourceId 资源ID
     * @return 上传结果
     */
    @PostMapping("/uploadWhiteList/{resourceId}")
    public CommonResult<String> uploadWhiteList(@RequestBody List<String> whiteList, @PathVariable(name = "resourceId") String resourceId) {
        return menuService.uploadWhiteList(whiteList, resourceId);
    }
}