package com.fcl.ccmall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.common.enums.OrderApplyStatusEnum;
import com.fcl.ccmall.common.enums.OrderStatusEnum;
import com.fcl.ccmall.dao.OrderReturnApplyDao;
import com.fcl.ccmall.entity.DO.OrderReturnApplyDO;
import com.fcl.ccmall.entity.dto.OrderReturnApplyListDTO;
import com.fcl.ccmall.entity.dto.RefuseApplyDTO;
import com.fcl.ccmall.entity.vo.OrderReturnApplyListVO;
import com.fcl.ccmall.mapper.OrderMasterMapper;
import com.fcl.ccmall.mapper.OrderReturnApplyMapper;
import com.fcl.ccmall.model.OrderMaster;
import com.fcl.ccmall.model.OrderReturnApply;
import com.fcl.ccmall.service.OrderReturnApplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class OrderReturnApplyServiceImpl implements OrderReturnApplyService {

    @Resource
    private OrderReturnApplyDao orderReturnApplyDao;

    @Resource
    private OrderReturnApplyMapper orderReturnApplyMapper;

    @Resource
    private OrderMasterMapper orderMasterMapper;

    @Override
    public CommonResult getApplyList(OrderReturnApplyListDTO dto) {
        Page<OrderReturnApplyDO> applyDOPage = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<OrderReturnApplyDO> applyList = orderReturnApplyDao.getApplyList(applyDOPage, dto);
        OrderReturnApplyListVO orderReturnApplyListVO = new OrderReturnApplyListVO();
        orderReturnApplyListVO.setList(applyList.getRecords());
        orderReturnApplyListVO.setTotal(applyDOPage.getTotal());
        return CommonResult.success(orderReturnApplyListVO);
    }

    @Override
    public CommonResult agreeApply(Integer id) {
        orderReturnApplyMapper.update(null, new LambdaUpdateWrapper<OrderReturnApply>()
        .eq(OrderReturnApply::getId, id)
        .set(OrderReturnApply::getStatus, OrderApplyStatusEnum.RETURNING.getCode())
        .set(OrderReturnApply::getHandleTime, LocalDateTime.now()));
        // 这里应该是调取远程退款服务成功的回调方法更新订单状态
        // 更新订单状态
        OrderReturnApply orderReturnApply = orderReturnApplyMapper.selectById(id);
        orderMasterMapper.update(null, new LambdaUpdateWrapper<OrderMaster>()
        .eq(OrderMaster::getId, orderReturnApply.getOrderId())
        .set(OrderMaster::getOrderStatus, OrderStatusEnum.CLOSED.getCode()));
        return CommonResult.success();
    }

    @Override
    public CommonResult refuseApply(RefuseApplyDTO refuseApplyDTO) {
        orderReturnApplyMapper.update(null, new LambdaUpdateWrapper<OrderReturnApply>()
        .eq(OrderReturnApply::getId, refuseApplyDTO.getId())
        .set(OrderReturnApply::getHandleNote, refuseApplyDTO.getHandleNote())
        .set(OrderReturnApply::getStatus, OrderApplyStatusEnum.REJECT.getCode())
        .set(OrderReturnApply::getHandleTime, LocalDateTime.now()));
        return CommonResult.success();
    }

    @Override
    public CommonResult finishApply(Integer id) {
        orderReturnApplyMapper.update(null, new LambdaUpdateWrapper<OrderReturnApply>()
        .eq(OrderReturnApply::getId, id)
        .set(OrderReturnApply::getStatus, OrderApplyStatusEnum.FINISHED.getCode()));
        return CommonResult.success();
    }
}
