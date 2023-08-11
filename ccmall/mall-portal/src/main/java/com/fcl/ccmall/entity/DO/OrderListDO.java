package com.fcl.ccmall.entity.DO;

import com.fcl.ccmall.model.OrderDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderListDO {
    private Integer orderId;
    private String orderSn;
    private Integer orderStatus;
    private BigDecimal totalPrice;
    private BigDecimal couponAmount;
    private List<OrderDetail> orderDetailList;
}
