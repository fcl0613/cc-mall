package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCountDO {
    private String dateTime;
    private Integer orderCount;
    private BigDecimal Income;
}
