package com.fcl.ccmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IncomeAnalysisEnum {
    LEVEL1("5千以下"),
    LEVEL2("5千到1万"),
    LEVEL3("1万到2万"),
    LEVEL4("2万到3万"),
    LEVEL5("3万以上");

    private String type;
}
