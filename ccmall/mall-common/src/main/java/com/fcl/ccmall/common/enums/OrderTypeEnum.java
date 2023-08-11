package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  OrderTypeEnum {
    DISTRIBUTION(0, "配送"),
    BY_SELF(1, "自提");

    private Integer code;
    private String description;

    public static String getDes(Integer code) {
        for (OrderTypeEnum value : OrderTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDescription();
            }
        }
        return null;
    }
}
