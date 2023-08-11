package com.fcl.ccmall.entity.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AliPayDTO {
    private Integer orderId;
    private BigDecimal price;
}
