package com.fcl.ccmall.entity.dto;

import lombok.Data;

@Data
public class GetCouponListDTO {
    private String name;
    private Integer type;
    private Long pageNum;
    private Long pageSize;
}
