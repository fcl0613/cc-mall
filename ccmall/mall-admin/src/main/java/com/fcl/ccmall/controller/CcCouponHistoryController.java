package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.GetCouponHistoryListDTO;
import com.fcl.ccmall.service.CcCouponHistoryService;
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
 * @since 2023-03-29
 */
@RestController
@RequestMapping("/coupon/history")
public class CcCouponHistoryController {
    @Resource
    private CcCouponHistoryService couponHistoryService;

    @PostMapping("/list")
    public CommonResult getCouponHistoryList(@RequestBody GetCouponHistoryListDTO dto) {
        return couponHistoryService.getCouponHistoryList(dto);
    }
}

