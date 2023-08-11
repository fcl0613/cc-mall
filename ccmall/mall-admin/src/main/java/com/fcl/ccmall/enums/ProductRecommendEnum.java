package com.fcl.ccmall.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductRecommendEnum {
    UN_RECOMMEND(0, "不推荐"),
    RECOMMEND(1, "推荐");

    private Integer code;
    private String description;
}
