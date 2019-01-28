package com.ssaw.ssawauthenticatecenterservice.entity;

import com.ssaw.supportjpa.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "tb_user_role")
public class UserRoleEntity extends BaseEntity implements Serializable {
    private Long userId;
    private Long roleId;
}
