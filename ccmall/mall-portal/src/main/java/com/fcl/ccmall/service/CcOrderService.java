package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.AfterSaleApplyDTO;
import com.fcl.ccmall.entity.DTO.CreateOrderDTO;
import com.fcl.ccmall.entity.DTO.GenerateConfirmOrderDTO;
import com.fcl.ccmall.entity.DTO.GetOrderListDTO;
import com.fcl.ccmall.entity.DTO.OrderDirectBuyDTO;
import com.fcl.ccmall.model.OrderMaster;

public interface CcOrderService extends IService<OrderMaster> {
    CommonResult generateConfirmOrder(GenerateConfirmOrderDTO generateConfirmOrderDTO);

    CommonResult createOrder(CreateOrderDTO createOrderDTO);

    CommonResult confirmOrder(Integer id);

    CommonResult getOrderList(GetOrderListDTO orderListDTO);

    CommonResult deleteOrder(Integer id);


    CommonResult cancelOrder(Integer id);

    CommonResult directConfirm(Integer productId, Integer count);

    CommonResult directBuy(OrderDirectBuyDTO orderDirectBuyDTO);
}
