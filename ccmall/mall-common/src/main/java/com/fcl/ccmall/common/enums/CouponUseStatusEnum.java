package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponUseStatusEnum {
    UN_USED(0, "未使用"),
    USED(1, "已使用"),
    EXPIRED(2, "已过期");

    private Integer code;
    private String description;

    public static String getDes(Integer code) {
        for (CouponUseStatusEnum value : CouponUseStatusEnum.values()) {
            if (value.equals(code)) {
                return value.getDescription();
            }
        }
        return null;
    }
}
