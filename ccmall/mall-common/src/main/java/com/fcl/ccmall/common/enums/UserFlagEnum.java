package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserFlagEnum {
    CUSTOMER("customer"),
    MANAGER("manage");

    private String description;
}
