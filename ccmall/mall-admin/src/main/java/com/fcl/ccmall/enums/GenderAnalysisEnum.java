package com.fcl.ccmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum GenderAnalysisEnum {
    MALE("男"),
    FEMALE("女"),
    UN_KNOW("未知");

    private String type;
}
