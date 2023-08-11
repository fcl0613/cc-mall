package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.model.CcCart;
import com.fcl.ccmall.model.CcCoupon;
import com.fcl.ccmall.model.CcDeliveryAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ConfirmOrderVO {
    private List<CcCart> cartList;
    // 可用优惠券
    private List<CcCoupon> hasCoupon;
    // 不可用优惠券
    private List<CcCoupon> noCoupon;
    private BigDecimal totalPrice;
//    private List<CcDeliveryAddress> deliveryAddressList;
}
