package com.ssaw.ssawuserresourceservice.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

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
    @OneToOne(targetEntity = ServiceEntity.class, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    private ServiceEntity service;

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
}
