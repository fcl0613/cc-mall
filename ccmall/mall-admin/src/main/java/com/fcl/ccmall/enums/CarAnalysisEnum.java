package com.fcl.ccmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarAnalysisEnum {
    HAS_CAR("有车"),
    NO_HAS_CAR("无车"),
    UN_KNOW("未知");

    private String type;
}
