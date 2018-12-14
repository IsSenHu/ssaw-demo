package com.ssaw.ssawauthenticatecenterservice.entity;

import com.ssaw.supportjpa.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen.
 * @date 2018/12/14 17:24.
 */
@Setter
@Getter
@Entity
@Table(name = "t_role")
public class RoleEntity extends BaseEntity {

}
