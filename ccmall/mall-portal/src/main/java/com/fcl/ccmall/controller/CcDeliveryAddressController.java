package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.model.CcDeliveryAddress;
import com.fcl.ccmall.service.CcDeliveryAddressService;
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
 * @since 2023-03-27
 */
@RestController
@RequestMapping("/address")
public class CcDeliveryAddressController {
    @Resource
    private CcDeliveryAddressService deliveryAddressService;

    @PostMapping("/add")
    public CommonResult create(@RequestBody CcDeliveryAddress deliveryAddress) {
        return deliveryAddressService.create(deliveryAddress);
    }

    @GetMapping("/list")
    public CommonResult getAddressList() {
        return deliveryAddressService.getAddressList();
    }

    @PostMapping("/remove/{id}")
    public CommonResult removeAddress(@PathVariable Integer id) {
        return deliveryAddressService.deleteAddress(id);
    }

    @PostMapping("/setDefault/{id}")
    public CommonResult setDefault(@PathVariable Integer id) {
        return deliveryAddressService.setDefault(id);
    }

    @PostMapping("/update")
    public CommonResult update(@RequestBody CcDeliveryAddress deliveryAddress) {
        return deliveryAddressService.updateAddress(deliveryAddress);
    }

    @GetMapping("/info/{id}")
    public CommonResult getAddressInfo(@PathVariable Integer id) {
        return deliveryAddressService.getAddressInfo(id);
    }

}

