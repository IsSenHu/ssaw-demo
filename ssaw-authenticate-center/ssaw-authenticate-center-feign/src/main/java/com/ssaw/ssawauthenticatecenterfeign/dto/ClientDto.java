package com.ssaw.ssawauthenticatecenterfeign.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen.
 * @date 2018/12/11 14:35.
 */
@Setter
@Getter
public class ClientDto implements Serializable {
    /** clientId、appKey */
    private String clientId;

    /** 用户ID */
    private Long userId;

    /** clientSecret */
    private String clientSecret;

    /** 资源ID */
    private String resourceIds;

    /** 作用域 */
    private String scopes;

    /** oauth2授权方式 */
    private String authorizedGrantTypes;

    /** 注册应用时的回调地址 */
    private String registeredRedirectUris;

    /** access token 过期时间 */
    private Integer accessTokenValiditySeconds;

    /** refresh token 过期时间 */
    private Integer refreshTokenValiditySeconds;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createMan;

    /**
     * 修改人
     */
    private String updateMan;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
