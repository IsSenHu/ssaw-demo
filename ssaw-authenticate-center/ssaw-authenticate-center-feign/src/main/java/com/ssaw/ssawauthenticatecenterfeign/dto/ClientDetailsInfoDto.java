package com.ssaw.ssawauthenticatecenterfeign.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author hszyp
 * @date 2019/01/27
 */
@Setter
@Getter
public class ClientDetailsInfoDto implements Serializable {
    /** clientId、appKey */
    private String clientId;

    /** 用户ID */
    private Long userId;

    /** 用户名 */
    private String username;

    /** clientSecret */
    private String clientSecret;

    /** 资源ID 如果为空，则表示不验证资源ID */
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createMan;

    /**
     * 修改人
     */
    private String updateMan;
}
