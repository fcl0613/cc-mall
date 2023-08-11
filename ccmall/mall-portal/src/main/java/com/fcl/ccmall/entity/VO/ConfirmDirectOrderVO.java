package com.fcl.ccmall.entity.VO;

import com.fcl.ccmall.model.CcCoupon;
import com.fcl.ccmall.model.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ConfirmDirectOrderVO {
    private List<Product> productList;
    private Integer count;
    // 可用优惠券
    private List<CcCoupon> hasCoupon;
    // 不可用优惠券
    private List<CcCoupon> noCoupon;
    private BigDecimal totalPrice;
}
