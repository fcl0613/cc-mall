package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderConfirmStatusEnum {

    UN_CONFIRM(0, "未确认"),
    CONFIRM(1, "已确认");

    private Integer code;
    private String description;
}
