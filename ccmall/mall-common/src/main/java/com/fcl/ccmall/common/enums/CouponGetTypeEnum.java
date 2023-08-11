package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponGetTypeEnum {
    SELF(0, "主动获取"),
    SYSTEM_SEND(1, "系统赠送");

    private Integer code;
    private String description;

    public static String getDes(Integer code) {
        for (CouponGetTypeEnum value : CouponGetTypeEnum.values()) {
            if (value.equals(code)) {
                return value.getDescription();
            }
        }
        return null;
    }
}
