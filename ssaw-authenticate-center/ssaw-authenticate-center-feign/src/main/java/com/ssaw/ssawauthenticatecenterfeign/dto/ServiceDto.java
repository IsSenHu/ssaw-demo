package com.ssaw.ssawauthenticatecenterfeign.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen.
 * @date 2018/12/11 13:44.
 */
@Setter
@Getter
public class ServiceDto implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 作用域
     */
    private String scopes;

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

    /**
     * 是否已被绑定
     */
    private Boolean isBind;
}
