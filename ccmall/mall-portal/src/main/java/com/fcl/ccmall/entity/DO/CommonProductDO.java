package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommonProductDO {
    private Integer id;
    private String productName;
    private String productCover;
    private BigDecimal price;
}
