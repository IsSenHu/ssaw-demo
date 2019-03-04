package com.ssaw.ssawauthenticatecenterservice.listen;

import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.event.local.UploadScopeAndWhiteListFinishedEvent;
import com.ssaw.ssawauthenticatecenterfeign.event.remote.*;
import com.ssaw.ssawauthenticatecenterfeign.vo.resource.UploadResourceVO;
import com.ssaw.ssawauthenticatecenterservice.service.MenuService;
import com.ssaw.ssawauthenticatecenterservice.service.ResourceService;
import com.ssaw.ssawauthenticatecenterservice.service.ScopeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.util.Assert;

import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;

/**
 * @author HuSen
 * @date 2019/3/2 11:49
 */
@Slf4j
@Configuration
public class SecuritySettingUploadListener {

    private final ApplicationContext context;
    private final ResourceService resourceService;
    private final ScopeService scopeService;
    private final MenuService menuService;

    @Autowired
    public SecuritySettingUploadListener(ApplicationContext context, ResourceService resourceService, ScopeService scopeService, MenuService menuService) {
        this.context = context;
        this.resourceService = resourceService;
        this.scopeService = scopeService;
        this.menuService = menuService;
    }

    // TODO 校验所有参数
    /**
     * 接收资源
     * @param event 上传资源远程事件
     */
    @EventListener(UploadResourceRemoteEvent.class)
    public void acceptResource(UploadResourceRemoteEvent event) {
        log.info("接收到上传资源远程事件:{}", JsonUtils.object2JsonString(event));
        Assert.notNull(event.getUploadResourceVO(), "上传的资源不能为NULL");

        UploadResourceFinishedRemoteEvent uploadResourceFinishedRemoteEvent = new UploadResourceFinishedRemoteEvent(
                context.getId(), context.getApplicationName().concat(":**"), event.getOriginService(), Boolean.TRUE, null
        );
        UploadResourceVO uploadResourceVO = event.getUploadResourceVO();
        CommonResult<UploadResourceVO> result = resourceService.uploadResource(uploadResourceVO);
        if (result.getCode() != SUCCESS) {
            uploadResourceFinishedRemoteEvent.setSuccess(Boolean.FALSE);
            context.publishEvent(uploadResourceFinishedRemoteEvent);
        } else {
            uploadResourceFinishedRemoteEvent.setUploadResourceVO(result.getData());
            context.publishEvent(uploadResourceFinishedRemoteEvent);
        }
    }

    /**
     * 接收作用域与白名单
     * @param event 上传作用域和白名单远程事件
     */
    @EventListener(UploadScopeAndWhiteListRemoteEvent.class)
    public void acceptScopeAndWhiteList(UploadScopeAndWhiteListRemoteEvent event) {
        log.info("接收到上传作用域和白名单远程事件:{}", JsonUtils.object2JsonString(event));
        Assert.notNull(event.getResourceId(), "资源ID不能为空");
        Assert.notNull(event.getScopeVOS(), "上传的作用域不能为NULL");
        Assert.notNull(event.getWhiteList(), "上传的作用域不能为NULL");

        CommonResult<String> uploadScopesResult = scopeService.uploadScopes(event.getScopeVOS());
        CommonResult<String> uploadWhiteListResult = menuService.uploadWhiteList(event.getWhiteList(), event.getResourceId());
        Assert.state(uploadScopesResult.getCode() == SUCCESS, "上传作用域失败");
        Assert.state(uploadWhiteListResult.getCode() == SUCCESS, "上传白名单失败");

        context.publishEvent(new UploadScopeAndWhiteListFinishedEvent(event.getResourceId()));
    }

    /**
     * 接收菜单和按钮
     * @param event 上传菜单和按钮远程事件
     */
    @EventListener(UploadMenuAndButtonRemoteEvent.class)
    public void acceptMenuAndButton(UploadMenuAndButtonRemoteEvent event) {
        log.info("接收到上传菜单和按钮远程事件:{}", JsonUtils.object2JsonString(event));
        Assert.notNull(event.getResourceId(), "资源ID不能为空");
        Assert.notNull(event.getMenuVO(), "菜单不能为空");
        Assert.notNull(event.getButtonVOS(), "按钮不能为空");

        CommonResult<String> uploadMenuResult = menuService.uploadMenus(event.getMenuVO(), event.getResourceId());
        CommonResult<String> uploadButtonResult = menuService.uploadButtons(event.getButtonVOS(), event.getResourceId());
        Assert.state(uploadMenuResult.getCode() == SUCCESS, "上传菜单失败");
        Assert.state(uploadButtonResult.getCode() == SUCCESS, "上传按钮失败");
    }

    /**
     * 上传完作用域和白名单后开始刷新作用域缓存
     * @param event 上传作用域与白名单完成事件
     */
    @EventListener(UploadScopeAndWhiteListFinishedEvent.class)
    public void settingFinished(UploadScopeAndWhiteListFinishedEvent event) {
        log.info("上传作用域与白名单完成事件:{}", JsonUtils.object2JsonString(event));
        scopeService.refreshScope((String) event.getSource());
    }
}