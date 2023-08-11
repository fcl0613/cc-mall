package com.fcl.ccmall.entity.DO;

import lombok.Data;

@Data
public class GetCouponHistoryListDO {
    private String username;
    private String getType;
    private String getTime;
    private String curStatus;
    private String useTime;
    private String orderId;
}
