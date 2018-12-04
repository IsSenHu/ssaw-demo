package com.ssaw.ssawuserresourceservice.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author HuSen.
 * @date 2018/11/27 17:46.
 */
@Setter
@Getter
@Entity
@Table(name = "tb_role",
    indexes = {@Index(name = "index_type", columnList = "type"),
            @Index(name = "index_is_parent", columnList = "is_parent"),
            @Index(name = "index_parent_id", columnList = "parent_id")}
)
public class RoleEntity {
    /**
     * 主键
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "unique_mark", unique = true, nullable = false)
    private String uniqueMark;

    @Column(name = "description")
    private String description;

    /** 权限类型 */
    @Column(name = "type", length = 1)
    private Integer type;
    /** 是否父节点 */
    @Column(name = "is_parent", length = 1)
    private Boolean isParent;
    /** 父节点ID */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
