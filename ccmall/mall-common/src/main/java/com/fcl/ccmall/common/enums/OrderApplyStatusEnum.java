package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderApplyStatusEnum {
    //0->待处理；1->退货中；2->已完成；3->已拒绝

    AWAIT_HANDLE(0, "待处理"),
    RETURNING(1, "退货中"),
    FINISHED(2, "已完成"),
    REJECT(3, "已拒绝"),
    DELETED(4, "删除");

    private Integer code;
    private String description;

    public static String getDes(Integer code) {
        for (OrderApplyStatusEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value.getDescription();
            }
        }
        return null;
    }
}
