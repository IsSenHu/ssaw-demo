package com.ssaw.ssawuserresourceservice.entity;

import lombok.Data;
import javax.persistence.*;

/**
 * @author HuSen.
 * @date 2018/11/27 19:53.
 */
@Data
@Entity
@Table(name = "tb_user_role", indexes = {@Index(name = "index_user_id", columnList = "user_id"), @Index(name = "index_role_id", columnList = "role_id")})
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;
}
