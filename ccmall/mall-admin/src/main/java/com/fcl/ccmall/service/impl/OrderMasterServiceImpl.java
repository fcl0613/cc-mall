package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.OrderDeleteStatusEnum;
import com.fcl.ccmall.common.enums.OrderStatusEnum;
import com.fcl.ccmall.common.enums.OrderTypeEnum;
import com.fcl.ccmall.dao.OrderDao;
import com.fcl.ccmall.entity.DO.OrderDetailProInfoDO;
import com.fcl.ccmall.entity.DO.OrderListDO;
import com.fcl.ccmall.entity.dto.GetOrderListDTO;
import com.fcl.ccmall.entity.dto.ShipmentsDTO;
import com.fcl.ccmall.entity.vo.OrderDetailVO;
import com.fcl.ccmall.entity.vo.OrderListVO;
import com.fcl.ccmall.mapper.OrderDetailMapper;
import com.fcl.ccmall.mapper.OrderMasterMapper;
import com.fcl.ccmall.model.OrderDetail;
import com.fcl.ccmall.model.OrderMaster;
import com.fcl.ccmall.service.OrderMasterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-03-28
 */
@Service
public class OrderMasterServiceImpl extends ServiceImpl<OrderMasterMapper, OrderMaster> implements OrderMasterService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Override
    public CommonResult getOrderList(GetOrderListDTO dto) {
        Page<OrderListDO> orderListDOPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<OrderListDO> orderList = orderDao.getOrderList(orderListDOPage, dto);
        OrderListVO orderListVO = new OrderListVO();
        orderListVO.setList(orderList.getRecords());
        orderListVO.setTotal(orderList.getTotal());
        return CommonResult.success(orderListVO);
    }

    @Override
    public CommonResult cancelOrder(Integer id) {
        OrderMaster orderMaster = this.getById(id);
        Integer orderStatus = orderMaster.getOrderStatus();
        if (OrderStatusEnum.OBLIGATION.getCode().equals(orderStatus)) {
            // 如果是未付款状态 直接更新
            this.update(null, new LambdaUpdateWrapper<OrderMaster>()
                    .eq(OrderMaster::getId, id)
                    .set(OrderMaster::getOrderStatus, OrderStatusEnum.CLOSED.getCode()));
            return CommonResult.success();
        }
        if (OrderStatusEnum.WAITING_FOR_DELIVERY.getCode().equals(orderStatus)) {
            // 如果是待发货状态 调取远程服务 退款成功后取消订单
            // TODO
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult getOrderDetail(Integer id) {
        OrderMaster orderMaster = this.getById(id);
        List<OrderDetail> orderDetailList =
                orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderId, id));
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        if (Objects.isNull(orderMaster.getCouponAmount())) {
            orderDetailVO.setCouponAmount(BigDecimal.ZERO);
        }else {
            orderDetailVO.setCouponAmount(orderMaster.getCouponAmount());
        }
        orderDetailVO.setDeliveryProvince(orderMaster.getReceiverProvince());
        orderDetailVO.setDeliveryCity(orderMaster.getReceiverCity());
        orderDetailVO.setDeliveryCounty(orderMaster.getReceiverCounty());
        orderDetailVO.setDeliveryAddress(orderMaster.getReceiverDetailAddress());
        orderDetailVO.setDeliveryName(orderMaster.getReceiverName());
        orderDetailVO.setDeliveryPhone(orderMaster.getReceiverPhone());
        orderDetailVO.setOrderId(orderMaster.getId());
        orderDetailVO.setOrderSn(orderMaster.getOrderId());
        orderDetailVO.setOrderStatus(orderMaster.getOrderStatus());
        orderDetailVO.setOrderStatusStr(OrderStatusEnum.getDes(orderMaster.getOrderStatus()));
        orderDetailVO.setOrderType(OrderTypeEnum.getDes(orderMaster.getOrderType()));
        orderDetailVO.setPayAmount(orderMaster.getPayAmount());
        orderDetailVO.setPoints(orderMaster.getIntegration());
        orderDetailVO.setTotalPrice(orderMaster.getTotalAmount());
        orderDetailVO.setUserAccount(orderMaster.getCustomerUsername());
        List<OrderDetailProInfoDO> list = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            OrderDetailProInfoDO detailProInfoDO = new OrderDetailProInfoDO();
            detailProInfoDO.setProductCount(orderDetail.getProductCnt());
            detailProInfoDO.setProductCover(orderDetail.getProductCover());
            detailProInfoDO.setProductName(orderDetail.getProductName());
            detailProInfoDO.setProductPrice(orderDetail.getProductPrice());
            detailProInfoDO.setSubtotal(orderDetail.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductCnt())));
            list.add(detailProInfoDO);
        }
        orderDetailVO.setList(list);
        return CommonResult.success(orderDetailVO);
    }

    @Override
    public CommonResult shipments(ShipmentsDTO shipmentsDTO) {
        this.update(null, new LambdaUpdateWrapper<OrderMaster>()
        .eq(OrderMaster::getOrderStatus, OrderStatusEnum.WAITING_FOR_DELIVERY.getCode())
        .in(OrderMaster::getId, shipmentsDTO.getOrderIds())
        .set(OrderMaster::getOrderStatus, OrderStatusEnum.DELIVERED.getCode())
        .set(OrderMaster::getDeliveryTime, LocalDateTime.now()));
        return CommonResult.success();
    }

    @Override
    public CommonResult deleteOrder(Integer id) {
        this.update(null, new LambdaUpdateWrapper<OrderMaster>()
        .eq(OrderMaster::getId, id)
        .set(OrderMaster::getDeleteStatus, OrderDeleteStatusEnum.DELETED.getCode()));
        return CommonResult.success();
    }
}
