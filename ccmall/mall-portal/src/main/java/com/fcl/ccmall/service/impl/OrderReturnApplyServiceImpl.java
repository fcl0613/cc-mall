package com.fcl.ccmall.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.OrderApplyStatusEnum;
import com.fcl.ccmall.common.exception.Asserts;
import com.fcl.ccmall.entity.DTO.AfterSaleApplyDTO;
import com.fcl.ccmall.entity.VO.OrderApplyListVO;
import com.fcl.ccmall.entity.VO.OrderApplyVO;
import com.fcl.ccmall.mapper.OrderDetailMapper;
import com.fcl.ccmall.mapper.OrderMasterMapper;
import com.fcl.ccmall.mapper.OrderReturnApplyMapper;
import com.fcl.ccmall.model.OrderDetail;
import com.fcl.ccmall.model.OrderMaster;
import com.fcl.ccmall.model.OrderReturnApply;
import com.fcl.ccmall.service.OrderReturnApplyService;
import com.fcl.ccmall.utils.JwtTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fcl
 * @since 2023-03-31
 */
@Service
public class OrderReturnApplyServiceImpl extends ServiceImpl<OrderReturnApplyMapper, OrderReturnApply> implements OrderReturnApplyService {

    @Resource
    private JwtTokenUtils jwtTokenUtils;

    @Resource
    private HttpServletRequest request;

    @Resource
    private OrderMasterMapper orderMasterMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Override
    public CommonResult create(AfterSaleApplyDTO afterSaleApplyDTO) {
        OrderMaster orderMaster = orderMasterMapper.selectById(afterSaleApplyDTO.getId());
        LocalDateTime localDateTime = LocalDateTime.now();
        // 比较订单日期
        ZoneId zoneId = ZoneId.systemDefault();
        long between = DateUtil.between(Date.from(orderMaster.getOrderTime().atZone(zoneId).toInstant()),
                Date.from(localDateTime.atZone(zoneId).toInstant()), DateUnit.DAY);
        if (between > 7) {
            Asserts.fail("您的订单已超过7天，无法进行退款");
        }
        OrderReturnApply orderReturnApply = new OrderReturnApply();
        orderReturnApply.setCreateTime(localDateTime);
        orderReturnApply.setCustomerId(getCustomerId());
        orderReturnApply.setOrderId(orderMaster.getId());
        orderReturnApply.setOrderSn(orderMaster.getOrderId());
        orderReturnApply.setReason(afterSaleApplyDTO.getReason());
        orderReturnApply.setReturnName(orderMaster.getReceiverName());
        orderReturnApply.setReturnPhone(orderMaster.getReceiverPhone());
        orderReturnApply.setReturnPrice(orderMaster.getPayAmount());
        orderReturnApply.setStatus(OrderApplyStatusEnum.AWAIT_HANDLE.getCode());
        this.save(orderReturnApply);
        return CommonResult.success();
    }

    @Override
    public CommonResult getApplyList(Long pageNum, Long pageSize) {
        OrderApplyVO orderApplyVO = new OrderApplyVO();
        Page<OrderReturnApply> applyPage = new Page<>(pageNum, pageSize);
        Page<OrderReturnApply> page = this.page(applyPage, new LambdaQueryWrapper<OrderReturnApply>()
                .eq(OrderReturnApply::getCustomerId, getCustomerId())
        .ne(OrderReturnApply::getStatus, OrderApplyStatusEnum.DELETED.getCode())
        .orderByDesc(OrderReturnApply::getId));
        List<OrderReturnApply> records = page.getRecords();
        if (records.size() == 0) {
            orderApplyVO.setList(new ArrayList<>());
            orderApplyVO.setTotal(0l);
            return CommonResult.success(orderApplyVO);
        }
        List<Integer> orderIdList = new ArrayList<>();
        for (OrderReturnApply record : records) {
            orderIdList.add(record.getOrderId());
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetail>()
                .in(OrderDetail::getOrderId, orderIdList));
        List<OrderApplyListVO> orderApplyListVOS = new ArrayList<>();
        for (OrderReturnApply record : records) {
            OrderApplyListVO orderApplyListVO = new OrderApplyListVO();
            orderApplyListVO.setId(record.getId());
            orderApplyListVO.setOrderSn(record.getOrderSn());
            orderApplyListVO.setStatus(record.getStatus());
            orderApplyListVO.setHandleNode(record.getHandleNote());
            orderApplyListVO.setStatusStr(OrderApplyStatusEnum.getDes(record.getStatus()));
            List<OrderDetail> list = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetailList) {
                if (record.getOrderId().equals(orderDetail.getOrderId())) {
                    list.add(orderDetail);
                }
            }
            orderApplyListVO.setOrderDetails(list);
            orderApplyListVOS.add(orderApplyListVO);
        }
        orderApplyVO.setList(orderApplyListVOS);
        orderApplyVO.setTotal(page.getTotal());
        return CommonResult.success(orderApplyVO);
    }

    @Override
    public CommonResult removeApply(Integer id) {
        this.update(null, new LambdaUpdateWrapper<OrderReturnApply>()
        .eq(OrderReturnApply::getId, id)
        .set(OrderReturnApply::getStatus, OrderApplyStatusEnum.DELETED.getCode()));
        return CommonResult.success();
    }

    private Integer getCustomerId() {
        return jwtTokenUtils.getUserId(request.getHeader("token"));
    }
}
