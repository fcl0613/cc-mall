package com.fcl.ccmall.entity.vo;

import com.fcl.ccmall.entity.DO.OrderDetailProInfoDO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDetailVO {
    private Integer orderId;
    private String orderSn;
    private String userAccount;
    private String orderType;
    private Integer points;
    private Integer orderStatus;
    private String orderStatusStr;
    private String deliveryName;
    private String deliveryPhone;
    private String deliveryProvince;
    private String deliveryCity;
    private String deliveryCounty;
    private String deliveryAddress;
    private BigDecimal totalPrice;
    private BigDecimal payAmount;
    private BigDecimal couponAmount;
    private List<OrderDetailProInfoDO> list;
}
