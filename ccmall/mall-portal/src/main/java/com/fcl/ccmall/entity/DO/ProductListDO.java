package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductListDO {
    private String productName;
    private String productCover;
    private Integer id;
    private BigDecimal price;
}
