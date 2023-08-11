package com.fcl.ccmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobStatusEnum {
    NOT_STARTED(0, "未开始"),
    EXECUTE(1, "执行中"),
    FAILED(2, "执行失败"),
    SUCCESS(3, "执行成功");


    private Integer code;
    private String description;

    public static String getDesc(Integer code) {
        for (JobStatusEnum value : JobStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDescription();
            }
        }
        return null;
    }
}
