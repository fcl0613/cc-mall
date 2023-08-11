package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetCouponListDO {
    private Integer id;

    /**
     * 优惠券类型0全场券1会员赠券2购物赠券3注册赠券
     */
    private String type;

    /**
     * 优惠券名
     */
    private String name;


    /**
     * 优惠金额
     */
    private BigDecimal amount;


    /**
     * 使用门槛(满减)
     */
    private BigDecimal minPoint;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 使用类型0全场通用1指定分类2指定商品
     */
    private String useType;

    private String status;
}
