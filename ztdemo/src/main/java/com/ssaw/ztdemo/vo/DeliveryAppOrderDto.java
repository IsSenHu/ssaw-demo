package com.ssaw.ztdemo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author HuSen.
 * @date 2018/11/22 11:21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryAppOrderDto implements Serializable {
    private static final long serialVersionUID = 2708569490776949574L;

    /**即配送活动标识，由外部系统生成*/
    private long deliveryId;
    /**订单id*/
    private String orderId;
    /**订单创建时间*/
    private Long createTime;
    /**门店id*/
    private String shopId;
    /**配送服务代码
     * 飞速达:4002
     * 快速达:4011
     * 及时达:4012
     * 集中送:4013*/
    private int deliveryServiceCode;
    /**配送优先级 1: 立即送  2: 普通送 3:自提*/
    private Integer shipOrder;
    /** 订单来源 */
    private String orderSource;
    /**收货人名称*/
    private String receiverName;
    /**收货地址*/
    private String receiverAddress;
    /**收货人电话*/
    private String receiverPhone;
    /**收货人经度*/
    private String receiverLng;
    /**收货人纬度*/
    private String receiverLat;
    /**0火星坐标，1百度坐标，默认0*/
    private int coordinateType;
    /**货物价格,单位cm，精确小数点后两位，范围为0-5000*/
    private BigDecimal goodsValue;
    /**货物高度，单位cm，精确小数点后两位，范围为0-45*/
    private BigDecimal goodsHeight;
    /**货物宽度，单位cm，精确小数点后两位，范围为0-50*/
    private BigDecimal goodsWidth;
    /**货物长度，单位cm，精确小数点后两位，范围为0-65*/
    private BigDecimal goodsLength;
    /**货物重量，单位kg，精确小数点后两位，范围为0-50*/
    private BigDecimal goodsWeight;
    /**货物详情*/
    private String goodsDetail;
    /**货物取货信息*/
    private String goodsPickupInfo;
    /**货物交付信息*/
    private String goodsDeliveryInfo;
    /**期望取货时间unix-timestamp*/
    private long expectedPickupTime;
    /**期望送达时间unix-timestamp
     * 飞速达	4002	发单时间 + 45mins
     * 快速达	4011	发单时间+1h
     * 及时达	4012	发单时间+2h
     * 集中送	4013	发单时间+2h*/
    private long expectedDeliveryTime;
    /**订单类型，默认为0
     0: 即时单(尽快送达，限当日订单)
     1: 预约单*/
    private int orderType;
    /**门店订单流水号，方便骑手门店取货*/
    private String poiSeq;
    /**备注，最长不超过200个字符。*/
    private String note;
    /**骑手应付金额，单位为元，精确到分*/
    private BigDecimal cashOnDelivery;
    /**骑手应收金额，单位为元，精确到分*/
    private BigDecimal cashOnPickup;
    /**发票抬头，最长不超过256个字符*/
    private String invoiceTitle;
    /** 物流公司 */
    private String logisticCompany;
    /** 订单商品 */
    private List<MeiTuanGoodsDto> meiTuanGoodsDtos;

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public Integer getShipOrder() {
        return shipOrder;
    }

    public void setShipOrder(Integer shipOrder) {
        this.shipOrder = shipOrder;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getGoodsHeight() {
        return goodsHeight;
    }

    public void setGoodsHeight(BigDecimal goodsHeight) {
        this.goodsHeight = goodsHeight;
    }

    public BigDecimal getGoodsWidth() {
        return goodsWidth;
    }

    public void setGoodsWidth(BigDecimal goodsWidth) {
        this.goodsWidth = goodsWidth;
    }

    public BigDecimal getGoodsLength() {
        return goodsLength;
    }

    public void setGoodsLength(BigDecimal goodsLength) {
        this.goodsLength = goodsLength;
    }

    public BigDecimal getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(BigDecimal goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public String getGoodsPickupInfo() {
        return goodsPickupInfo;
    }

    public void setGoodsPickupInfo(String goodsPickupInfo) {
        this.goodsPickupInfo = goodsPickupInfo;
    }

    public String getGoodsDeliveryInfo() {
        return goodsDeliveryInfo;
    }

    public void setGoodsDeliveryInfo(String goodsDeliveryInfo) {
        this.goodsDeliveryInfo = goodsDeliveryInfo;
    }

    public String getPoiSeq() {
        return poiSeq;
    }

    public void setPoiSeq(String poiSeq) {
        this.poiSeq = poiSeq;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getCashOnDelivery() {
        return cashOnDelivery;
    }

    public void setCashOnDelivery(BigDecimal cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }

    public BigDecimal getCashOnPickup() {
        return cashOnPickup;
    }

    public void setCashOnPickup(BigDecimal cashOnPickup) {
        this.cashOnPickup = cashOnPickup;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getDeliveryServiceCode() {
        return deliveryServiceCode;
    }

    public void setDeliveryServiceCode(int deliveryServiceCode) {
        this.deliveryServiceCode = deliveryServiceCode;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverLng() {
        return receiverLng;
    }

    public void setReceiverLng(String receiverLng) {
        this.receiverLng = receiverLng;
    }

    public String getReceiverLat() {
        return receiverLat;
    }

    public void setReceiverLat(String receiverLat) {
        this.receiverLat = receiverLat;
    }

    public int getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(int coordinateType) {
        this.coordinateType = coordinateType;
    }

    public BigDecimal getGoodsValue() {
        return goodsValue;
    }

    public void setGoodsValue(BigDecimal goodsValue) {
        this.goodsValue = goodsValue;
    }

    public long getExpectedPickupTime() {
        return expectedPickupTime;
    }

    public void setExpectedPickupTime(long expectedPickupTime) {
        this.expectedPickupTime = expectedPickupTime;
    }

    public long getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(long expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getLogisticCompany() {
        return logisticCompany;
    }

    public void setLogisticCompany(String logisticCompany) {
        this.logisticCompany = logisticCompany;
    }

    public List<MeiTuanGoodsDto> getMeiTuanGoodsDtos() {
        return meiTuanGoodsDtos;
    }

    public void setMeiTuanGoodsDtos(List<MeiTuanGoodsDto> meiTuanGoodsDtos) {
        this.meiTuanGoodsDtos = meiTuanGoodsDtos;
    }
}
