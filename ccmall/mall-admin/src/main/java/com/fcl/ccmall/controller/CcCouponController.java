package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.BatchRemoveDTO;
import com.fcl.ccmall.entity.dto.CreateCouponDTO;
import com.fcl.ccmall.entity.dto.GetCouponListDTO;
import com.fcl.ccmall.entity.dto.UpdateCouponDTO;
import com.fcl.ccmall.service.CcCouponService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-03-28
 */
@RestController
@RequestMapping("/coupon")
public class CcCouponController {

    @Resource
    private CcCouponService couponService;

    @PostMapping("/create")
    public CommonResult createCoupon(@RequestBody CreateCouponDTO createCouponDTO) {
        return couponService.createCoupon(createCouponDTO);
    }

    @PostMapping("/update")
    public CommonResult updateCoupon(@RequestBody UpdateCouponDTO updateCouponDTO) {
        return couponService.updateCoupon(updateCouponDTO);
    }

    @PostMapping("/remove")
    public CommonResult deleteCoupon(@RequestBody BatchRemoveDTO batchRemoveDTO) {
        return couponService.deleteCoupon(batchRemoveDTO);
    }

    @PostMapping("/list")
    public CommonResult getCouponList(@RequestBody GetCouponListDTO getCouponListDTO) {
        return couponService.getCouponList(getCouponListDTO);
    }

    /**
     * 用于优惠券编辑
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public CommonResult getCouponInfo(@PathVariable Integer id) {
        return couponService.getCouponInfo(id);
    }

    /**
     * 用于领取详情
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public CommonResult GetCouponDetail(@PathVariable Integer id) {
        return couponService.getCouponDetail(id);
    }
}

