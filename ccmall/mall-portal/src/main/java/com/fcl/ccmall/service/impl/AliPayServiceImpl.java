package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.OrderStatusEnum;
import com.fcl.ccmall.entity.DTO.AliPayDTO;
import com.fcl.ccmall.mapper.OrderMasterMapper;
import com.fcl.ccmall.model.OrderMaster;
import com.fcl.ccmall.service.AliPayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class AliPayServiceImpl implements AliPayService {

    @Resource
    private OrderMasterMapper orderMasterMapper;

    @Override
    public CommonResult aliPay() {

        return null;
    }

    @Override
    public CommonResult payNotify(AliPayDTO aliPayDTO) {
        // 这里模拟支付宝回调成功修改订单状态为待发货状态
        OrderMaster orderMaster = orderMasterMapper.selectById(aliPayDTO.getOrderId());
        if (OrderStatusEnum.OBLIGATION.getCode().equals(orderMaster.getOrderStatus())) {
            orderMasterMapper.update(null, new LambdaUpdateWrapper<OrderMaster>()
            .eq(OrderMaster::getId, aliPayDTO.getOrderId())
            .set(OrderMaster::getOrderStatus, OrderStatusEnum.WAITING_FOR_DELIVERY.getCode())
            .set(OrderMaster::getPaymentTime, LocalDateTime.now()));
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult returnPay(AliPayDTO aliPayDTO) {
        orderMasterMapper.update(null, new LambdaUpdateWrapper<OrderMaster>()
                .eq(OrderMaster::getId, aliPayDTO.getOrderId())
                .set(OrderMaster::getOrderStatus, OrderStatusEnum.CLOSED.getCode()));
        return CommonResult.success();
    }
}
