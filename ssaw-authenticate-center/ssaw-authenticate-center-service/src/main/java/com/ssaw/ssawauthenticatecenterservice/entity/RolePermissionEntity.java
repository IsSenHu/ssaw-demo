package com.ssaw.ssawauthenticatecenterservice.entity;

import com.ssaw.supportjpa.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen.
 * @date 2018/12/14 20:13.
 */
@Setter
@Getter
@Entity
@Table(name = "t_role_permission")
public class RolePermissionEntity extends BaseEntity {
    private Long roleId;
    private Long permissionId;
}
