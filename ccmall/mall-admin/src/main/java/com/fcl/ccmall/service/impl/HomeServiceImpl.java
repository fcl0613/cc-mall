package com.fcl.ccmall.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.OrderApplyStatusEnum;
import com.fcl.ccmall.common.enums.OrderDeleteStatusEnum;
import com.fcl.ccmall.common.enums.OrderStatusEnum;
import com.fcl.ccmall.common.enums.ProductPublishEnum;
import com.fcl.ccmall.dao.HomeDao;
import com.fcl.ccmall.entity.DO.OrderCountDO;
import com.fcl.ccmall.entity.vo.HomeContentVO;
import com.fcl.ccmall.mapper.CustomerMapper;
import com.fcl.ccmall.mapper.OrderMasterMapper;
import com.fcl.ccmall.mapper.OrderReturnApplyMapper;
import com.fcl.ccmall.mapper.ProductMapper;
import com.fcl.ccmall.model.OrderMaster;
import com.fcl.ccmall.model.OrderReturnApply;
import com.fcl.ccmall.model.Product;
import com.fcl.ccmall.service.HomeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    private final String DATE_FORMAT = "yyyy-MM-dd";

    @Resource
    private OrderMasterMapper orderMasterMapper;

    @Resource
    private HomeDao homeDao;

    @Resource
    private OrderReturnApplyMapper orderReturnApplyMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CustomerMapper customerMapper;


    @Override
    public CommonResult getContent() {
        String today = DateUtil.format(DateUtil.date(), DATE_FORMAT);
        String yesterday = DateUtil.format(DateUtil.yesterday(), DATE_FORMAT);

        HomeContentVO homeContentVO = new HomeContentVO();
        homeContentVO.setTodayOrderCount(homeDao.todayOrderCount(today));
        homeContentVO.setYesterdayIncome(homeDao.getIncome(yesterday));
        homeContentVO.setTodayIncome(homeDao.getIncome(today));
        homeContentVO.setObligationOrderCount(orderMasterMapper.selectCount(new LambdaQueryWrapper<OrderMaster>()
        .eq(OrderMaster::getOrderStatus, OrderStatusEnum.OBLIGATION.getCode())
        .eq(OrderMaster::getDeleteStatus, OrderDeleteStatusEnum.UN_DELETED.getCode())));
        homeContentVO.setWaitingFoDeliveryOrderCount(orderMasterMapper.selectCount(new LambdaQueryWrapper<OrderMaster>()
        .eq(OrderMaster::getOrderStatus, OrderStatusEnum.WAITING_FOR_DELIVERY.getCode())));
        homeContentVO.setDeliveredOrderCount(orderMasterMapper.selectCount(new LambdaQueryWrapper<OrderMaster>()
        .eq(OrderMaster::getOrderStatus, OrderStatusEnum.DELIVERED.getCode())));
        homeContentVO.setFinishedOrderCount(orderMasterMapper.selectCount(new LambdaQueryWrapper<OrderMaster>()
        .eq(OrderMaster::getOrderStatus, OrderStatusEnum.FINISHED.getCode())));
        homeContentVO.setWaitingOrderApplyCount(orderReturnApplyMapper.selectCount(new LambdaQueryWrapper<OrderReturnApply>()
        .eq(OrderReturnApply::getStatus, OrderApplyStatusEnum.AWAIT_HANDLE.getCode())));
        homeContentVO.setProductCount(productMapper.selectCount(null));
        homeContentVO.setProductPublishCount(productMapper.selectCount(new LambdaQueryWrapper<Product>()
        .eq(Product::getPublishStatus, ProductPublishEnum.PUBLISH.getCode())));
        homeContentVO.setProductUnPublishCount(productMapper.selectCount(new LambdaQueryWrapper<Product>()
                .eq(Product::getPublishStatus, ProductPublishEnum.UN_PUBLISH.getCode())));
        homeContentVO.setCustomerCount(customerMapper.selectCount(null));
        return CommonResult.success(homeContentVO);
    }

    @Override
    public CommonResult getOrderTable() {
        List<OrderCountDO> orderCountDOList = new ArrayList<>();
        DateTime dateTime = DateUtil.offsetDay(DateUtil.date(), -7);
        for (int i = 0; i < 7; i++) {
            String format = DateUtil.format(DateUtil.offsetDay(dateTime, i), DATE_FORMAT);
            OrderCountDO orderCount = homeDao.getOrderCount(format);
            orderCount.setDateTime(format);
            orderCountDOList.add(orderCount);
        }
        return CommonResult.success(orderCountDOList);
    }
}
