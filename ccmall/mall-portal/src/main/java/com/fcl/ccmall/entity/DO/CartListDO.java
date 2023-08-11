package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartListDO {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer productAmount;
    private String productCover;
    private BigDecimal price;
}
