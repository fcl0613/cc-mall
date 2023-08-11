package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCategoryLevelEnum {
    FATHER_LEVEL(0, "父级菜单"),
    CHILD(1, "二级菜单");

    private Integer code;
    private String description;
}
