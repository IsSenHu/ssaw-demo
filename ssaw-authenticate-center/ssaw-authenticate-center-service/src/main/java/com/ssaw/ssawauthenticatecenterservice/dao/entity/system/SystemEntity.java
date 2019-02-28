package com.ssaw.ssawauthenticatecenterservice.dao.entity.system;

import com.ssaw.supportjpa.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author HuSen
 * @date 2019/2/28 17:07
 */
@Getter
@Setter
@Entity
@Table(name = "tb_system")
public class SystemEntity extends BaseEntity implements Serializable {

    /** 系统名称 */
    private String name;

    /** key */
    private String keyP;

    /** 密码 */
    private String secretP;
}