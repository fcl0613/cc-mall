package com.fcl.ccmall.entity.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {
    private Integer addressId;
    private Integer couponId;
    private Integer orderType;
    private Integer payType;
    // 购物车的编号
    private List<Integer> cartIds;
}
