package com.fcl.ccmall.controller;

import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.CreateOrderDTO;
import com.fcl.ccmall.entity.DTO.GenerateConfirmOrderDTO;
import com.fcl.ccmall.entity.DTO.GetOrderListDTO;
import com.fcl.ccmall.entity.DTO.OrderDirectBuyDTO;
import com.fcl.ccmall.service.CcOrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
public class CcOrderController {
    @Resource
    private CcOrderService orderService;

    /**
     * 根据购物车信息创建确认订单
     * @param generateConfirmOrderDTO
     * @return
     */
    @PostMapping("/generateConfirmOrder")
    public CommonResult generateConfirmOrder(@RequestBody GenerateConfirmOrderDTO generateConfirmOrderDTO) {
        return orderService.generateConfirmOrder(generateConfirmOrderDTO);
    }

    /**
     * 创建订单
     * @return
     */
    @PostMapping("/create")
    public CommonResult createOrder(@RequestBody CreateOrderDTO createOrderDTO) {
        return orderService.createOrder(createOrderDTO);
    }

    /**
     * 确认收货
     * @return
     */
    @PostMapping("/confirm/{id}")
    public CommonResult confirm(@PathVariable Integer id) {
        return orderService.confirmOrder(id);
    }

    /**
     * 订单列表
     * @return
     */
    @PostMapping("/list")
    public CommonResult getOrderList(@RequestBody GetOrderListDTO orderListDTO) {
        return orderService.getOrderList(orderListDTO);
    }

    /**
     * 删除订单
     * @param id
     * @return
     */
    @PostMapping("/remove/{id}")
    public CommonResult removeOrder(@PathVariable Integer id) {
        return orderService.deleteOrder(id);
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @PostMapping("/cancel/{id}")
    public CommonResult cancelOrder(@PathVariable Integer id) {
        return orderService.cancelOrder(id);
    }

    /**
     * 立即购买创建确认单
     * @param productId
     * @param count
     * @return
     */
    @PostMapping("/direct/confirm")
    public CommonResult directConfirm(@RequestParam("productId") Integer productId,
                                      @RequestParam("count") Integer count) {
        return orderService.directConfirm(productId, count);
    }

    /**
     * 立即购买
     * @param orderDirectBuyDTO
     * @return
     */
    @PostMapping("/direct/buy")
    public CommonResult directBuy(@RequestBody OrderDirectBuyDTO orderDirectBuyDTO) {
        return orderService.directBuy(orderDirectBuyDTO);
    }

}
