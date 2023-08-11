package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductPublishEnum {
    PUBLISH(0, "上架"),
    UN_PUBLISH(1, "下架");

    private Integer code;
    private String description;
}
