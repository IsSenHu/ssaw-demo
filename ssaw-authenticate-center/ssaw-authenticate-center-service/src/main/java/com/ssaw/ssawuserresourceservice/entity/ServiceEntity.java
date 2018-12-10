package com.ssaw.ssawuserresourceservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
     * 该服务所关联的资源
     */
    private ResourceEntity resource;
}
