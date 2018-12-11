package com.ssaw.ssawuserresourceservice.entity;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.*;

/**
 * @author HuSen.
 * @date 2018/12/10 20:15.
 */
@Setter
@Entity
@Table(name = "tb_client_details")
public class ClientDetailsEntity implements ClientDetails {

    /** clientId、appKey */
    @Id
    private String clientId;

    /** 用户ID */
    @Column(name = "user_id")
    private Long userId;

    /** clientSecret */
    @Column(name = "client_secret")
    private String clientSecret;

    /** 资源ID */
    @Column(name = "resource_ids")
    private String resourceIds;

    /** 作用域 */
    @Column(name = "scopes")
    private String scopes;

    /** oauth2类型 */
    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    /** 注册应用时的回调地址 */
    @Column(name = "registered_redirect_uris")
    private String registeredRedirectUris;

    /** access token 过期时间 */
    @Column(name = "access_token_validity_seconds")
    private Integer accessTokenValiditySeconds;

    /** refresh token 过期时间 */
    @Column(name = "refresh_token_validity_seconds")
    private Integer refreshTokenValiditySeconds;

    @Override
    public String getClientId() {
        return clientId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Set<String> getResourceIds() {
        return StringUtils.isNotBlank(resourceIds)
                ? new HashSet<>(Arrays.asList(resourceIds.split(",")))
                : new HashSet<>(0);
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return StringUtils.isNotBlank(scopes)
                ? new HashSet<>(Arrays.asList(scopes.split(",")))
                : new HashSet<>(0);
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return StringUtils.isNotEmpty(authorizedGrantTypes)
                ? new HashSet<>(Arrays.asList(authorizedGrantTypes.split(",")))
                : new HashSet<>(0);
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return StringUtils.isNotBlank(registeredRedirectUris)
                ? new HashSet<>(Arrays.asList(registeredRedirectUris.split(",")))
                : new HashSet<>(0);
    }

    /**
     * 不使用
     * @return 该应用的权限
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>(0);
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return true;
    }

    /**
     * 放一些描述信息，可能有用，现在不用
     * @return 描述信息
     */
    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
