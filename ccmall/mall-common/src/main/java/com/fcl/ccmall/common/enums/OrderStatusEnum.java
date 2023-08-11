package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OrderStatusEnum {
    OBLIGATION(0, "待付款"),
    WAITING_FOR_DELIVERY(1, "待发货"),
    DELIVERED(2, "已发货"),
    FINISHED(3, "已完成"),
    CLOSED(4, "已关闭"),
    canceled(5, "无效订单");

    private Integer code;
    private String description;

    public static String getDes(Integer code) {
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDescription();
            }
        }
        return null;
    }
}
