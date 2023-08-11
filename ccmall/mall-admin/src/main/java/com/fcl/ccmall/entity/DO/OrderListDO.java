package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderListDO {
    private Integer id;
    private String orderId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer orderStatus;
    private LocalDateTime orderTime;
    private Integer orderType;
    private String customerUsername;
}
