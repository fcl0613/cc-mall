package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponTypeEnum {
    // 0全场券1会员赠券2购物赠券3注册赠券

    ALL(0, "全场券"),
    NUMBER(1, "会员赠券"),
    SHOPPING(2, "购物赠券"),
    REGISTER(3, "注册赠券");

    private Integer code;
    private String description;

    public static String getDes(Integer code) {
        for (CouponTypeEnum value : CouponTypeEnum.values()) {
            if (value.getCode() == code) {
                return value.getDescription();
            }
        }
        return null;
    }
}
