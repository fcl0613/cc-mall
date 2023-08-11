package com.fcl.ccmall.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HomeContentVO {
    private Integer todayOrderCount;
    private BigDecimal todayIncome;
    private BigDecimal yesterdayIncome;
    private Integer obligationOrderCount;
    private Integer waitingFoDeliveryOrderCount;
    private Integer deliveredOrderCount;
    private Integer finishedOrderCount;
    private Integer waitingOrderApplyCount;
    private Integer productCount;
    private Integer productPublishCount;
    private Integer productUnPublishCount;
    private Integer customerCount;
}
