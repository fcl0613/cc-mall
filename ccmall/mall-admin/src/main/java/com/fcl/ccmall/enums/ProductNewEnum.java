package com.fcl.ccmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductNewEnum {
    NEW(1, "新品"),
    UN_NEW(0, "不是新品");

    private Integer code;
    private String description;
}
