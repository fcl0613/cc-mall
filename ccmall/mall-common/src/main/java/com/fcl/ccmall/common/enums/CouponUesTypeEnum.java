package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponUesTypeEnum {

    ALL(0, "全场通用"),
    ASSIGNED_CATEGORY(1, "指定分类"),
    ASSIGNED_PRODUCT(2, "指定商品");

    private Integer type;
    private String describe;

    public static String getDes(Integer type) {
        for (CouponUesTypeEnum value : CouponUesTypeEnum.values()) {
            if (value.getType() == type) {
                return value.getDescribe();
            }
        }
        return null;
    }
}
