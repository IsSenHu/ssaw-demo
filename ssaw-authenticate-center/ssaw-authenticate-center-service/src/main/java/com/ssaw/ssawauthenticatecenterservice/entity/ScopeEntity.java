package com.ssaw.ssawauthenticatecenterservice.entity;

import com.ssaw.supportjpa.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

/**
 * @author HuSen.
 * @date 2018/12/12 10:20.
 */
@Setter
@Getter
@Entity
@Table(name = "t_scope")
public class ScopeEntity extends BaseEntity {
    @Column(unique = true)
    private String scope;

    @Column
    private String uri;

    @Column
    private Long resourceId;
}
