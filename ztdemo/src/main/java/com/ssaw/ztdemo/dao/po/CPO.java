package com.ssaw.ztdemo.dao.po;

import javax.persistence.*;

/**
 * @author hszyp
 */
@Entity
@Table(name = "tb_oms_consignee")
public class CPO {

    @Id
    private Long id;

    @Column(name = "order_bn")
    private String orderBn;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderBn() {
        return orderBn;
    }

    public void setOrderBn(String orderBn) {
        this.orderBn = orderBn;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "CPO{" +
                "id=" + id +
                ", orderBn='" + orderBn + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
