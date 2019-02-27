package com.ssaw.ztdemo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 美团配送单货物详情信息
 * @author HuSen
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeiTuanGoodsDto implements Serializable {
    private static final long serialVersionUID = 489843470527253658L;
    /** 商品编号*/
    private String bn;
    /** 商品名称*/
    private String name;
    /** 商品原价*/
    private BigDecimal price;
    /** 商品总价*/
    private BigDecimal amount;
    /** 商品重量*/
    private BigDecimal weight;
    /** 商品购买价格*/
    private BigDecimal gPrice;
    /** 商品/赠品*/
    private String productType;
    /** 商品类型(商品/赠品) 0: 商品 1 赠品*/
    private Integer productTypeInt;
    /** 是否属于自身维护编号,
     *  如果为true 则表示该编号是自身维护 需要从数据库中
     *  根据短编号进行查询获取
     * */
    private Boolean isOwnBn;
    /** 货物数量 */
    private int goodCount;
    /** 订单编号 */
    private String orderBn;
    /** 货品名称 */
    private String goodName;
    /** 货品单价 */
    private BigDecimal goodPrice;
    /** 货品单位 */
    private String goodUnit;

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public BigDecimal getGoodPrice() {
        return goodPrice;
    }

    public void setGoodPrice(BigDecimal goodPrice) {
        this.goodPrice = goodPrice;
    }

    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getgPrice() {
        return gPrice;
    }

    public void setgPrice(BigDecimal gPrice) {
        this.gPrice = gPrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getProductTypeInt() {
        return productTypeInt;
    }

    public void setProductTypeInt(Integer productTypeInt) {
        this.productTypeInt = productTypeInt;
    }

    public Boolean getOwnBn() {
        return isOwnBn;
    }

    public void setOwnBn(Boolean ownBn) {
        isOwnBn = ownBn;
    }

    public String getOrderBn() {
        return orderBn;
    }

    public void setOrderBn(String orderBn) {
        this.orderBn = orderBn;
    }

    public String getGoodUnit() {
        return goodUnit;
    }

    public void setGoodUnit(String goodUnit) {
        this.goodUnit = goodUnit;
    }
}
