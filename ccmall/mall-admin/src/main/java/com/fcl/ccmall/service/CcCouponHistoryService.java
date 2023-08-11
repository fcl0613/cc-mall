package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.GetCouponHistoryListDTO;
import com.fcl.ccmall.model.CcCouponHistory;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-03-29
 */
public interface CcCouponHistoryService extends IService<CcCouponHistory> {

    CommonResult getCouponHistoryList(GetCouponHistoryListDTO dto);
}
