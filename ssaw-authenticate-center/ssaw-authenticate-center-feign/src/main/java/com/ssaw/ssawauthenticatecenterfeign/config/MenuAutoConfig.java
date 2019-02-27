package com.ssaw.ssawauthenticatecenterfeign.config;

import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.dto.Button;
import com.ssaw.ssawauthenticatecenterfeign.dto.Menu;
import com.ssaw.ssawauthenticatecenterfeign.feign.AuthenticateFeign;
import com.ssaw.ssawauthenticatecenterfeign.properties.EnableResourceAutoProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
public class MenuAutoConfig {

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private final EnableResourceAutoProperties enableResourceAutoProperties;

    private final AuthenticateFeign authenticateFeign;

    private final ApplicationContext context;

    @Autowired
    public MenuAutoConfig(EnableResourceAutoProperties enableResourceAutoProperties, AuthenticateFeign authenticateFeign, ApplicationContext context) {
        this.enableResourceAutoProperties = enableResourceAutoProperties;
        this.authenticateFeign = authenticateFeign;
        this.context = context;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        if (!initialized.getAndSet(true)) {
            Menu menu = new Menu();
            menu.setIndex(enableResourceAutoProperties.getCode());
            menu.setTemplate(new Menu.Template(enableResourceAutoProperties.getIcon(), enableResourceAutoProperties.getDescription()));
            Map<String, Object> beansWithAnnotation = context.getBeansWithAnnotation(SecurityApi.class);
            List<SecurityApi> securityApiList = beansWithAnnotation.values().stream().map(o -> o.getClass().getAnnotation(SecurityApi.class)).collect(Collectors.toList());
            List<Menu> menuItems = securityApiList.stream().collect(Collectors.groupingBy(securityApi -> securityApi.index().concat("_").concat(securityApi.group()))).entrySet().stream().map(entry -> {
                String[] s = entry.getKey().split("_");
                Menu m1 = new Menu();
                m1.setIndex(enableResourceAutoProperties.getCode().concat("-").concat(s[0]));
                m1.setTemplate(new Menu.Template("", s[1]));
                List<Menu> items = new ArrayList<>();
                entry.getValue().stream().map(SecurityApi::menu).forEach(menus -> Arrays.stream(menus).forEach(m -> {
                    Menu m2 = new Menu();
                    m2.setTitle(m.title());
                    m2.setIndex(enableResourceAutoProperties.getCode().concat("-").concat(m.index()));
                    m2.setScope(enableResourceAutoProperties.getResourceId().concat("_").concat(m.scope()));
                    m2.setTo(m.to());
                    items.add(m2);
                }));
                m1.setItems(items);
                return m1;
            }).collect(Collectors.toList());
            menu.setItems(menuItems);

            log.info("开始上传菜单:{}", JsonUtils.object2JsonString(menu));
            CommonResult<String> result = authenticateFeign.uploadMenus(menu, enableResourceAutoProperties.getResourceId());
            Assert.state(result.getCode() == SUCCESS, "上传菜单失败");

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

            List<Button> buttons = securityMethods.stream().map(securityMethod -> {
                Button button = new Button();
                button.setKey(securityMethod.button());
                button.setScope(enableResourceAutoProperties.getResourceId().concat("_").concat(securityMethod.scope()));
                button.setName(securityMethod.buttonName());
                return button;
            }).collect(Collectors.toList());

            log.info("开始上传按钮:{}", JsonUtils.object2JsonString(buttons));
            CommonResult<String> commonResult = authenticateFeign.uploadButtons(buttons, enableResourceAutoProperties.getResourceId());
            Assert.state(commonResult.getCode() == SUCCESS, "上传按钮失败");
        }
    }
}