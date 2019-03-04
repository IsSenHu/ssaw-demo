package com.ssaw.ssawauthenticatecenterfeign.config;

import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.event.remote.AuthenticateCenterEventBasicPackage;
import com.ssaw.ssawauthenticatecenterfeign.event.remote.UploadMenuAndButtonRemoteEvent;
import com.ssaw.ssawauthenticatecenterfeign.vo.ButtonVO;
import com.ssaw.ssawauthenticatecenterfeign.vo.MenuVO;
import com.ssaw.ssawauthenticatecenterfeign.feign.AuthenticateFeign;
import com.ssaw.ssawauthenticatecenterfeign.properties.EnableResourceAutoProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.ssaw.commons.constant.Constants.ResultCodes.SUCCESS;

/**
 * @author HuSen
 * @date 2019/2/26 11:27
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(EnableResourceAutoProperties.class)
@ConditionalOnMissingClass("com.ssaw.ssawauthenticatecenterservice.conditional.ConditionalOnMissingClass")
@RemoteApplicationEventScan(basePackageClasses = AuthenticateCenterEventBasicPackage.class)
public class MenuAutoUploadConfiguration {

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private final EnableResourceAutoProperties enableResourceAutoProperties;

    private final AuthenticateFeign authenticateFeign;

    private final ApplicationContext context;

    @Autowired
    public MenuAutoUploadConfiguration(EnableResourceAutoProperties enableResourceAutoProperties, AuthenticateFeign authenticateFeign, ApplicationContext context) {
        this.enableResourceAutoProperties = enableResourceAutoProperties;
        this.authenticateFeign = authenticateFeign;
        this.context = context;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        CommonResult<String> applicationName = authenticateFeign.getApplicationName();
        Assert.state(applicationName.getCode() == SUCCESS, "获取认证中心服务名称失败");

        if (!initialized.getAndSet(true)) {
            MenuVO menuVO = new MenuVO();
            menuVO.setIndex(enableResourceAutoProperties.getCode());
            menuVO.setTemplate(new MenuVO.Template(enableResourceAutoProperties.getIcon(), enableResourceAutoProperties.getDescription()));
            Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(SecurityApi.class);
            List<SecurityApi> securityApiList = beansWithAnnotation.values().stream().map(o -> o.getClass().getAnnotation(SecurityApi.class)).collect(Collectors.toList());
            List<MenuVO> menuVOItems = securityApiList.stream().collect(Collectors.groupingBy(securityApi -> securityApi.index().concat("_").concat(securityApi.group()))).entrySet().stream().map(entry -> {
                String[] s = entry.getKey().split("_");
                MenuVO m1 = new MenuVO();
                m1.setIndex(enableResourceAutoProperties.getCode().concat("-").concat(s[0]));
                m1.setTemplate(new MenuVO.Template("", s[1]));
                List<MenuVO> items = new ArrayList<>();
                entry.getValue().stream().map(SecurityApi::menu).forEach(menus -> Arrays.stream(menus).forEach(m -> {
                    MenuVO m2 = new MenuVO();
                    m2.setTitle(m.title());
                    m2.setIndex(enableResourceAutoProperties.getCode().concat("-").concat(m.index()));
                    m2.setScope(enableResourceAutoProperties.getResourceId().concat("_").concat(m.scope()));
                    m2.setTo(m.to());
                    items.add(m2);
                }));
                m1.setItems(items);
                return m1;
            }).collect(Collectors.toList());
            menuVO.setItems(menuVOItems);

            List<SecurityMethod> securityMethods = new ArrayList<>();
            for (Object bean : beansWithAnnotation.values()) {
                Method[] declaredMethods = bean.getClass().getMethods();
                for (Method method : declaredMethods) {
                    SecurityMethod securityMethod = AnnotationUtils.findAnnotation(method, SecurityMethod.class);
                    if (Objects.nonNull(securityMethod) && StringUtils.isNotBlank(securityMethod.button())) {
                        securityMethods.add(securityMethod);
                    }
                }
            }

            List<ButtonVO> buttonVOS = securityMethods.stream().map(securityMethod -> {
                ButtonVO buttonVO = new ButtonVO();
                buttonVO.setKey(securityMethod.button());
                buttonVO.setScope(enableResourceAutoProperties.getResourceId().concat("_").concat(securityMethod.scope()));
                buttonVO.setName(securityMethod.buttonName());
                return buttonVO;
            }).collect(Collectors.toList());

            log.info("开始上传菜单和按钮:{}-{}", JsonUtils.object2JsonString(menuVO), JsonUtils.object2JsonString(buttonVOS));
            UploadMenuAndButtonRemoteEvent uploadMenuAndButtonRemoteEvent = new UploadMenuAndButtonRemoteEvent(
                    context.getId(), context.getApplicationName().concat(":**"), applicationName.getData().concat(":**"),
                    enableResourceAutoProperties.getResourceId(), menuVO, buttonVOS
            );
            context.publishEvent(uploadMenuAndButtonRemoteEvent);
        }
    }
}