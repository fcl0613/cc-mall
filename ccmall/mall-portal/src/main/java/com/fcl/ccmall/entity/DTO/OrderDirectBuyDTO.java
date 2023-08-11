package com.fcl.ccmall.entity.DTO;

import lombok.Data;

@Data
public class OrderDirectBuyDTO {
    private Integer addressId;
    private Integer couponId;
    private Integer productId;
    private Integer count;
    private Integer orderType;
    private Integer payType;
}
