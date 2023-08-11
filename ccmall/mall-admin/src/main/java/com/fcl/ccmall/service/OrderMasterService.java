package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.GetOrderListDTO;
import com.fcl.ccmall.entity.dto.ShipmentsDTO;
import com.fcl.ccmall.model.OrderMaster;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-03-28
 */
public interface OrderMasterService extends IService<OrderMaster> {

    CommonResult getOrderList(GetOrderListDTO dto);

    CommonResult cancelOrder(Integer id);

    CommonResult getOrderDetail(Integer id);

    CommonResult shipments(ShipmentsDTO shipmentsDTO);

    CommonResult deleteOrder(Integer id);
}
