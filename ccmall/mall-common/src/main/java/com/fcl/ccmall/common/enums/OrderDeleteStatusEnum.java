package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderDeleteStatusEnum {
    UN_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    private Integer code;
    private String description;
}
