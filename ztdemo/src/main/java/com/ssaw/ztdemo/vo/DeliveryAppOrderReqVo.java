package com.ssaw.ztdemo.vo;

import java.io.Serializable;

/**
 * 根据订单号查询订单信息给配送APP请求VO
 * @author HuSen.
 * @date 2018/11/23 10:08.
 */
public class DeliveryAppOrderReqVo implements Serializable {
    private static final long serialVersionUID = 4847205992825342581L;

    /** 订单号 */
    private String orderBn;

    public String getOrderBn() {
        return orderBn;
    }

    public void setOrderBn(String orderBn) {
        this.orderBn = orderBn;
    }
}
