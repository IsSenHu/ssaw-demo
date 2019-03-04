package com.ssaw.ssawauthenticatecenterfeign.config;

import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.event.remote.AuthenticateCenterEventBasicPackage;
import com.ssaw.ssawauthenticatecenterfeign.event.remote.UploadResourceFinishedRemoteEvent;
import com.ssaw.ssawauthenticatecenterfeign.event.remote.UploadResourceRemoteEvent;
import com.ssaw.ssawauthenticatecenterfeign.event.remote.UploadScopeAndWhiteListRemoteEvent;
import com.ssaw.ssawauthenticatecenterfeign.vo.resource.UploadResourceVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.scope.ScopeVO;
import com.ssaw.ssawauthenticatecenterfeign.feign.AuthenticateFeign;
import com.ssaw.ssawauthenticatecenterfeign.properties.EnableResourceAutoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;

/**
 * @author HuSen
 * @date 2019/2/25 17:41
 */
@Slf4j
@Configuration
@EnableScheduling
@EnableConfigurationProperties(EnableResourceAutoProperties.class)
@ConditionalOnMissingClass("com.ssaw.ssawauthenticatecenterservice.conditional.ConditionalOnMissingClass")
@RemoteApplicationEventScan(basePackageClasses = AuthenticateCenterEventBasicPackage.class)
public class ResourceScopeWhiteListAutoUploadConfiguration {

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private final ApplicationContext context;

    private final EnableResourceAutoProperties enableResourceAutoProperties;

    private final AuthenticateFeign authenticateFeign;

    private String destination;

    @Autowired
    public ResourceScopeWhiteListAutoUploadConfiguration(EnableResourceAutoProperties enableResourceAutoProperties, AuthenticateFeign authenticateFeign, ApplicationContext context) {
        this.enableResourceAutoProperties = enableResourceAutoProperties;
        this.authenticateFeign = authenticateFeign;
        this.context = context;
    }

    /**
     * 在项目启动完成后开始资源服务的上传
     */
    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        if (!initialized.getAndSet(true)) {
            log.info("资源服务上传, 权限上传>>>>>>开始");

            Assert.hasText(enableResourceAutoProperties.getResourceId(), "资源ID不能为空");
            Assert.hasText(enableResourceAutoProperties.getDescription(), "资源描述不能为空");
            Assert.hasText(enableResourceAutoProperties.getCode(), "资源编码不能为空");
            CommonResult<String> applicationName = authenticateFeign.getApplicationName();
            Assert.state(applicationName.getCode() == SUCCESS, "获取认证中心服务名称失败");
            destination = applicationName.getData().concat(":**");

            final UploadResourceVO resource = new UploadResourceVO();
            resource.setResourceId(enableResourceAutoProperties.getResourceId());
            resource.setDescription(enableResourceAutoProperties.getDescription());

            log.info("上传资源服务:{}", JsonUtils.object2JsonString(resource));
            UploadResourceRemoteEvent uploadResourceRemoteEvent = new UploadResourceRemoteEvent(
                    context.getId(), context.getApplicationName().concat(":**"), destination, resource);
            context.publishEvent(uploadResourceRemoteEvent);
        }
    }

    /**
     * 监听资源上传完成远程事件，并完成作用域与白名单的上传
     * @param event 资源上传完成远程事件
     */
    @EventListener(UploadResourceFinishedRemoteEvent.class)
    public void uploadResourceFinishedRemoteListener(UploadResourceFinishedRemoteEvent event) {
        Assert.isTrue(event.getSuccess(), "上传资源服务到认证中心失败");

        UploadResourceVO uploadResourceVO = event.getUploadResourceVO();
        List<SecurityMethod> securityMethods = new ArrayList<>();
        List<ScopeVO> scopeVOList = new ArrayList<>();
        List<Menu> menus = new ArrayList<>();
        Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(SecurityApi.class);
        for (Object bean : beansWithAnnotation.values()) {
            SecurityApi securityApi = AnnotationUtils.findAnnotation(bean.getClass(), SecurityApi.class);
            if (Objects.nonNull(securityApi)) {
                menus.addAll(Arrays.stream(securityApi.menu()).collect(Collectors.toList()));
            }
            Method[] declaredMethods = bean.getClass().getMethods();
            for (Method method : declaredMethods) {
                SecurityMethod securityMethod = AnnotationUtils.findAnnotation(method, SecurityMethod.class);
                if (Objects.nonNull(securityMethod)) {
                    securityMethods.add(securityMethod);
                }
            }
        }

        Set<Map.Entry<String, List<SecurityMethod>>> entries = securityMethods.stream().collect(Collectors.groupingBy(SecurityMethod::scope)).entrySet();
        for (Map.Entry<String, List<SecurityMethod>> entry : entries) {
            String antMatchers = String.join(",", entry.getValue().stream().map(SecurityMethod::antMatcher).collect(Collectors.toList()).toArray(new String[]{}));
            ScopeVO scopeVO = new ScopeVO();
            scopeVO.setResourceId(uploadResourceVO.getId());
            scopeVO.setResourceName(uploadResourceVO.getResourceId());
            scopeVO.setScope(enableResourceAutoProperties.getResourceId().concat("_").concat(entry.getKey()));
            scopeVO.setUri(antMatchers);
            scopeVOList.add(scopeVO);
        }

        for (Menu menu : menus) {
            ScopeVO scopeVO = new ScopeVO();
            scopeVO.setResourceId(uploadResourceVO.getId());
            scopeVO.setResourceName(uploadResourceVO.getResourceId());
            scopeVO.setScope(enableResourceAutoProperties.getResourceId().concat("_").concat(menu.scope()));
            scopeVO.setUri(UUID.randomUUID().toString());
            scopeVOList.add(scopeVO);
        }
        List<String> whiteList = enableResourceAutoProperties.getWhiteList();
        log.info("上传作用域以及白名单:{}-{}", JsonUtils.object2JsonString(scopeVOList), whiteList);

        UploadScopeAndWhiteListRemoteEvent uploadScopeAndWhiteListRemoteEvent = new UploadScopeAndWhiteListRemoteEvent(
                context.getId(), context.getApplicationName().concat(":**"), destination, uploadResourceVO.getResourceId(), scopeVOList, whiteList
        );
        context.publishEvent(uploadScopeAndWhiteListRemoteEvent);
    }
}