package com.ssaw.ssawuserresourceservice.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author HuSen.
 * @date 2018/12/10 20:05.
 */
@Setter
@Getter
@Entity
@Table(name = "tb_service")
public class ServiceEntity {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务名称
     */
    @Column(name = "service_name", unique = true)
    private String serviceName;

    /**
     * 作用域
     */
    @Column(name = "scopes")
    private String scopes;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @Column(name = "create_man")
    private String createMan;

    /**
     * 修改人
     */
    @Column(name = "update_man")
    private String updateMan;

    /**
     * 是否已被绑定
     */
    @Column(name = "is_bind")
    private Boolean isBind;
}
