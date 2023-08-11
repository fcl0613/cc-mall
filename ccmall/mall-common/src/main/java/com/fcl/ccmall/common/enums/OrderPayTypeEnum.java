package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderPayTypeEnum {
    ALIPAY(1, "支付宝"),
    WE_CHAT(2, "微信"),
    CHINA_UNION_PAY(3, "银联"),
    CASH_ON_DELIVERY(4, "货到付款(自提订单可当面交付)");

    private Integer code;
    private String description;
}
