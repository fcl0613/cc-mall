package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.CartItemDTO;
import com.fcl.ccmall.service.CcCartService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author fcl
 * @since 2023-03-24
 */
@RestController
@RequestMapping("/cart")
public class CcCartController {

    @Resource
    private CcCartService cartService;

    @PostMapping("/add")
    public CommonResult addCart(@RequestBody CartItemDTO cartItemDTO) {
        return cartService.addCart(cartItemDTO);
    }

    @PostMapping("/remove/{id}")
    public CommonResult remove(@PathVariable Integer id) {
        return cartService.delete(id);
    }

    @PostMapping("/plus/{id}")
    public CommonResult plusCount(@PathVariable Integer id) {
        return cartService.plusCount(id);
    }

    @PostMapping("/minus/{id}")
    public CommonResult minusCount(@PathVariable Integer id) {
        return cartService.minusCount(id);
    }

    @GetMapping("/list")
    public CommonResult getCartList(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Long pageNum,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "100") Long pageSize) {
        return cartService.getCartList(pageNum, pageSize);
    }
}

