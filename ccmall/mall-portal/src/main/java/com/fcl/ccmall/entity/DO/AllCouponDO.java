package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AllCouponDO {
    // 优惠券id
    private Integer id;
    private BigDecimal amount;
    private String name;
    private String effectTime;
    // 优惠券使用类型
    private Integer useType;
    private Integer categoryId;
    private Integer productId;
}
