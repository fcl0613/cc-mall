package com.fcl.ccmall.entity.DTO;

import lombok.Data;

@Data
public class AfterSaleApplyDTO {
    // 订单编号
    private Integer id;
    // 退款原因
    private String reason;
}
