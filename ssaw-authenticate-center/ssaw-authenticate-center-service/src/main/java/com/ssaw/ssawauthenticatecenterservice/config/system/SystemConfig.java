package com.ssaw.ssawauthenticatecenterservice.config.system;

import com.ssaw.ssawauthenticatecenterfeign.event.local.AppFinishedEvent;
import com.ssaw.ssawauthenticatecenterservice.constants.client.ClientConstant;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.client.ClientDetailsEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.resource.ResourceEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.entity.scope.ScopeEntity;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.client.ClientRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.resource.ResourceRepository;
import com.ssaw.ssawauthenticatecenterservice.dao.repository.scope.ScopeRepository;
import com.ssaw.ssawauthenticatecenterservice.properties.SpringSummerAutumnWinterManageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @date 2019/3/1 10:36
 */
@Configuration
@EnableConfigurationProperties(SpringSummerAutumnWinterManageProperties.class)
public class SystemConfig {

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private final ResourceRepository resourceRepository;
    private final ScopeRepository scopeRepository;
    private final ClientRepository clientRepository;
    private final SpringSummerAutumnWinterManageProperties properties;
    private final ApplicationContext applicationContext;

    @Autowired
    public SystemConfig(SpringSummerAutumnWinterManageProperties properties, ResourceRepository resourceRepository, ScopeRepository scopeRepository, ClientRepository clientRepository, ApplicationContext applicationContext) {
        this.properties = properties;
        this.resourceRepository = resourceRepository;
        this.scopeRepository = scopeRepository;
        this.clientRepository = clientRepository;
        this.applicationContext = applicationContext;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        if (!initialized.getAndSet(true)) {
            Optional<ClientDetailsEntity> optional = clientRepository.findById(properties.getClientId());
            ClientDetailsEntity clientDetailsEntity;
            clientDetailsEntity = optional.orElseGet(ClientDetailsEntity::new);
            List<ResourceEntity> allResource = resourceRepository.findAll();
            List<ScopeEntity> allScope = scopeRepository.findAll();
            String resourceIds = allResource.stream().map(ResourceEntity::getResourceId).collect(Collectors.joining(","));
            String scopes = allScope.stream().map(ScopeEntity::getScope).collect(Collectors.joining(","));

            clientDetailsEntity.setClientId(properties.getClientId());
            clientDetailsEntity.setClientSecret(applicationContext.getBean(PasswordEncoder.class).encode(properties.getClientSecret()));
            clientDetailsEntity.setAuthorizedGrantTypes(ClientConstant.AuthorizedGrantTypes.AUTHORIZATION_CODE.getValue());
            clientDetailsEntity.setAccessTokenValiditySeconds(properties.getClientExpire());
            clientDetailsEntity.setRegisteredRedirectUris(properties.getClientRegisteredRedirectUris());
            clientDetailsEntity.setResourceIds(resourceIds);
            clientDetailsEntity.setScopes(scopes);
            clientDetailsEntity.setCreateTime(LocalDateTime.now());
            clientDetailsEntity.setUpdateTime(LocalDateTime.now());
            clientRepository.save(clientDetailsEntity);
        }
    }

    @EventListener(AppFinishedEvent.class)
    public void refreshClient(AppFinishedEvent appFinishedEvent) {
        Optional<ClientDetailsEntity> optional = clientRepository.findById(properties.getClientId());
        optional.ifPresent(clientDetailsEntity -> {
            List<ResourceEntity> allResource = resourceRepository.findAll();
            List<ScopeEntity> allScope = scopeRepository.findAll();
            String resourceIds = allResource.stream().map(ResourceEntity::getResourceId).collect(Collectors.joining(","));
            String scopes = allScope.stream().map(ScopeEntity::getScope).collect(Collectors.joining(","));

            clientDetailsEntity.setClientId(properties.getClientId());
            clientDetailsEntity.setClientSecret(applicationContext.getBean(PasswordEncoder.class).encode(properties.getClientSecret()));
            clientDetailsEntity.setAuthorizedGrantTypes(ClientConstant.AuthorizedGrantTypes.AUTHORIZATION_CODE.getValue());
            clientDetailsEntity.setAccessTokenValiditySeconds(properties.getClientExpire());
            clientDetailsEntity.setRegisteredRedirectUris(properties.getClientRegisteredRedirectUris());
            clientDetailsEntity.setResourceIds(resourceIds);
            clientDetailsEntity.setScopes(scopes);
            clientDetailsEntity.setCreateTime(LocalDateTime.now());
            clientDetailsEntity.setUpdateTime(LocalDateTime.now());
            clientRepository.save(clientDetailsEntity);
        });
    }
}