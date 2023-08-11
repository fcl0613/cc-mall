package com.fcl.ccmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeAnalysisEnum {

    CHILD("童年"),
    JUVENILE("少年"),
    TEENAGER("青少年"),
    MIDDLE_AGED("中年"),
    OLD_AGE("老年");


    private String type;
}
