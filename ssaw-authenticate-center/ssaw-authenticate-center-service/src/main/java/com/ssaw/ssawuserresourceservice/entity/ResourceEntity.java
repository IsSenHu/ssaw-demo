package com.ssaw.ssawuserresourceservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author HuSen.
 * @date 2018/12/10 20:00.
 */
@Setter
@Getter
@Entity
@Table(name = "tb_resource")
public class ResourceEntity {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资源唯一标识
     */
    @Column(name = "unique_mark", unique = true)
    private String uniqueMark;

    /**
     * 该资源所关联的服务
     */
    private ServiceEntity service;
}
