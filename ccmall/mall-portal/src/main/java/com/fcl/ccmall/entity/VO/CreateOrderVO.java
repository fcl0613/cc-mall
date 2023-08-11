package com.fcl.ccmall.entity.VO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderVO {
    private Integer orderId;
    private BigDecimal price;
    private String name;
}
