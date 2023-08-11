package com.fcl.ccmall.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum GenderEnums {
    MAN(0, "男"),
    WOMAN(1, "女"),
    UN_KNOW(2, "未知");

    private Integer type;
    private String describe;

    public static Map<Integer,String> getGenderMap() {
        HashMap<Integer, String> map = new HashMap<>();
        for (GenderEnums value : GenderEnums.values()) {
            map.put(value.type, value.getDescribe());
        }
        return map;
    }

    public static String getDescribeByType(Integer type) {
        for (GenderEnums value : GenderEnums.values()) {
           if (value.type == type) {
               return value.describe;
           }
        }
        return GenderEnums.UN_KNOW.describe;
    }
}
