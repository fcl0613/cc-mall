package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailProInfoDO {
    private String productCover;
    private String productName;
    private Integer productCount;
    private BigDecimal productPrice;
    private BigDecimal subtotal;
}
