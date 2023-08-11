package com.fcl.ccmall.service;

import com.fcl.ccmall.common.api.CommonResult;

public interface CouponService {
    CommonResult getAllCoupon();

    CommonResult receiveCoupon(Integer id);
}
