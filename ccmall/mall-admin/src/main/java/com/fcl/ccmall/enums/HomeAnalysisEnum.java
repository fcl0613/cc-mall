package com.fcl.ccmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HomeAnalysisEnum {

    HAS_HOME("有房"),
    NO_HAS_HOME("无房"),
    UN_KNOW("未知");

    private String type;
}
