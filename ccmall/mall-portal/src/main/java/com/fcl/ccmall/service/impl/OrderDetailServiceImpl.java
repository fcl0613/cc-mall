package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.mapper.OrderDetailMapper;
import com.fcl.ccmall.model.OrderDetail;
import com.fcl.ccmall.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
