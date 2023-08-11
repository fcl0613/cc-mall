package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.OrderStatusEnum;
import com.fcl.ccmall.entity.dto.AliPayDTO;
import com.fcl.ccmall.mapper.OrderMasterMapper;
import com.fcl.ccmall.model.OrderMaster;
import com.fcl.ccmall.service.AliPayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AliPayServiceImpl implements AliPayService {

    @Resource
    private OrderMasterMapper orderMasterMapper;

    @Override
    public CommonResult returnPay(AliPayDTO aliPayDTO) {
        orderMasterMapper.update(null, new LambdaUpdateWrapper<OrderMaster>()
                .eq(OrderMaster::getId, aliPayDTO.getOrderId())
                .set(OrderMaster::getOrderStatus, OrderStatusEnum.CLOSED.getCode()));
        return CommonResult.success();
    }
}
