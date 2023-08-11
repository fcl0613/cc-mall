package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.DTO.AfterSaleApplyDTO;
import com.fcl.ccmall.model.OrderReturnApply;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-03-31
 */
public interface OrderReturnApplyService extends IService<OrderReturnApply> {

    CommonResult create(AfterSaleApplyDTO afterSaleApplyDTO);

    CommonResult getApplyList(Long pageNum, Long pageSize);

    CommonResult removeApply(Integer id);
}
