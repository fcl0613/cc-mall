package com.fcl.ccmall.entity.dto;

import lombok.Data;

@Data
public class GetCouponHistoryListDTO {
    private Integer couponId;
    private Integer useStatus;
    private String orderId;
    private Long pageNum;
    private Long pageSize;
}
