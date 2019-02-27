package com.ssaw.ssawauthenticatecenterfeign.config;

import com.ssaw.commons.util.json.jack.JsonUtils;
import com.ssaw.commons.vo.CommonResult;
import com.ssaw.ssawauthenticatecenterfeign.annotations.Menu;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityApi;
import com.ssaw.ssawauthenticatecenterfeign.annotations.SecurityMethod;
import com.ssaw.ssawauthenticatecenterfeign.dto.ResourceDto;
import com.ssaw.ssawauthenticatecenterfeign.dto.ScopeDto;
import com.ssaw.ssawauthenticatecenterfeign.event.UploadScopeFinishedEvent;
import com.ssaw.ssawauthenticatecenterfeign.feign.AuthenticateFeign;
import com.ssaw.ssawauthenticatecenterfeign.properties.EnableResourceAutoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
public class ResourceAutoConfig  {

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private final ApplicationContext context;

    private final EnableResourceAutoProperties enableResourceAutoProperties;

    private final AuthenticateFeign authenticateFeign;

    @Autowired
    public ResourceAutoConfig(EnableResourceAutoProperties enableResourceAutoProperties, AuthenticateFeign authenticateFeign, ApplicationContext context) {
        this.enableResourceAutoProperties = enableResourceAutoProperties;
        this.authenticateFeign = authenticateFeign;
        this.context = context;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        if (!initialized.getAndSet(true)) {
            log.info("资源服务上报, 权限上报>>>>>>开始");

            Assert.hasText(enableResourceAutoProperties.getResourceId(), "资源ID不能为空");
            Assert.hasText(enableResourceAutoProperties.getDescription(), "资源描述不能为空");
            Assert.hasText(enableResourceAutoProperties.getCode(), "资源编码不能为空");

            ResourceDto resource = new ResourceDto();
            resource.setResourceId(enableResourceAutoProperties.getResourceId());
            resource.setDescription(enableResourceAutoProperties.getDescription());
            log.info("上传资源服务:{}", JsonUtils.object2JsonString(resource));
            CommonResult<ResourceDto> result = authenticateFeign.uploadResource(resource);
            Assert.state(result.getCode() == SUCCESS, "上传资源服务失败");

            List<SecurityMethod> securityMethods = new ArrayList<>();
            List<ScopeDto> scopeDtoList = new ArrayList<>();
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
                ScopeDto scopeDto = new ScopeDto();
                scopeDto.setResourceId(result.getData().getId());
                scopeDto.setResourceName(result.getData().getResourceId());
                scopeDto.setScope(enableResourceAutoProperties.getResourceId().concat("_").concat(entry.getKey()));
                scopeDto.setUri(antMatchers);
                scopeDtoList.add(scopeDto);
            }

            for (Menu menu : menus) {
                ScopeDto scopeDto = new ScopeDto();
                scopeDto.setResourceId(result.getData().getId());
                scopeDto.setResourceName(result.getData().getResourceId());
                scopeDto.setScope(enableResourceAutoProperties.getResourceId().concat("_").concat(menu.scope()));
                scopeDto.setUri(UUID.randomUUID().toString());
                scopeDtoList.add(scopeDto);
            }

            log.info("上传作用域:{}", JsonUtils.object2JsonString(scopeDtoList));
            CommonResult<String> commonResult = authenticateFeign.uploadScopes(scopeDtoList);
            Assert.state(commonResult.getCode() == SUCCESS, "上传作用域失败");

            context.publishEvent(new UploadScopeFinishedEvent(enableResourceAutoProperties.getResourceId()));
        }
    }
}