package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.service.CouponService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Resource
    private CouponService couponService;

    @GetMapping("/all")
    public CommonResult getAllCoupon() {
        return couponService.getAllCoupon();
    }

    @PostMapping("/receive/{id}")
    public CommonResult receiveCoupon(@PathVariable Integer id) {
        return couponService.receiveCoupon(id);
    }
}
