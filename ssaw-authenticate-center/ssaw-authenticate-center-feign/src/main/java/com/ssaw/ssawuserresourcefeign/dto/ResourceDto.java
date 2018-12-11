package com.ssaw.ssawuserresourcefeign.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen.
 * @date 2018/12/11 14:08.
 */
@Setter
@Getter
public class ResourceDto implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 资源唯一标识
     */
    private String uniqueMark;

    /**
     * 该资源所关联的服务
     */
    private ServiceDto service;

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
}
