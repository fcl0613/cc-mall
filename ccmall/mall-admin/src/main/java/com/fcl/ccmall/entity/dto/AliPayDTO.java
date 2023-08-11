package com.fcl.ccmall.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AliPayDTO {
    private Integer orderId;
    private BigDecimal price;
}
