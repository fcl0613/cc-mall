package com.fcl.ccmall.controller;


import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.GetOrderListDTO;
import com.fcl.ccmall.entity.dto.ShipmentsDTO;
import com.fcl.ccmall.service.OrderMasterService;
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
@RequestMapping("/order")
public class OrderMasterController {

    @Resource
    private OrderMasterService orderMasterService;

    @PostMapping("/list")
    public CommonResult getOrderList(@RequestBody GetOrderListDTO dto) {
        return orderMasterService.getOrderList(dto);
    }

    @PostMapping("/cancel/{id}")
    public CommonResult cancelOrder(@PathVariable Integer id) {
        return orderMasterService.cancelOrder(id);
    }

    @GetMapping("/detail/{id}")
    public CommonResult getOrderDetail(@PathVariable Integer id) {
        return orderMasterService.getOrderDetail(id);
    }

    @PostMapping("/shipments")
    public CommonResult shipments(@RequestBody ShipmentsDTO shipmentsDTO) {
        return orderMasterService.shipments(shipmentsDTO);
    }

    @PostMapping("/delete/{id}")
    public CommonResult deleteOrder(@PathVariable Integer id) {
        return orderMasterService.deleteOrder(id);
    }
}

