package com.fcl.ccmall.entity.VO;

import lombok.Data;

@Data
public class MyPageContentVO {
    private String avatar;
    private String nickName;
    private Integer point;
    /**
     * 待支付订单数量
     */
    private Integer pendingPayment;
    /**
     * 待发货订单数量
     */
    private Integer waitingForDelivery;
    /**
     * 待收货订单数量
     */
    private Integer shipped;
    /**
     * 待评价商品数量
     */
    private Integer waitingComments;
    /**
     * 售后订单数量
     */
    private Integer afterSales;
}
