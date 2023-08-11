package com.fcl.ccmall.entity.DO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderReturnApplyDO {
    private Integer id;
    private LocalDateTime createTime;
    private BigDecimal returnPrice;
    private Integer status;
    private String orderSn;
    private LocalDateTime handleTime;
}
