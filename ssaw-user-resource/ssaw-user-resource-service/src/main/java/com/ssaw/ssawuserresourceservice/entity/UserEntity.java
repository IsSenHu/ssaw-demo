package com.ssaw.ssawuserresourceservice.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author HuSen.
 * @date 2018/11/27 17:33.
 */
@Getter
@Setter
@Entity
@Table(name = "tb_user",
        indexes = {@Index(name = "index_is_enable", columnList = "is_enable")}
)
public class UserEntity {

    /**
     * 主键
     **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "is_enable", length = 1)
    private Boolean isEnable;

    @Column(name = "real_name", length = 50)
    private String realName;

    @Column(name = "description")
    private String description;

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
