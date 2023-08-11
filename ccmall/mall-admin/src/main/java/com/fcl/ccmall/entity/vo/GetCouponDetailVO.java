package com.fcl.ccmall.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetCouponDetailVO {
    private String name;
    private String type;
    private String useType;
    private BigDecimal minPoint;
    private BigDecimal amount;
    private String status;
    private String effectiveTime;
    /**
     * 优惠券数量
     */
    private Integer couponCount;
    /**
     * 优惠券领取数量
     */
    private Integer receiveCount;

    /**
     * 未领取数量
     */
    private Integer unReceiveCount;

    /**
     * 使用数量
     */
    private Integer useCount;

    /**
     * 未使用数量=领取数量-使用数量
     */
    private Integer unUseCount;
}
