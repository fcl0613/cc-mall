package com.fcl.ccmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DefaultAddressFlagEnum {
    DEFAULT(0, "默认"),
    COMMON(1, "普通地址");
    private Integer code;
    private String description;
}
