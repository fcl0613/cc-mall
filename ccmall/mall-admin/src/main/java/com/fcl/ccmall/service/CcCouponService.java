package com.fcl.ccmall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fcl.ccmall.common.api.CommonResult;
import com.fcl.ccmall.entity.dto.BatchRemoveDTO;
import com.fcl.ccmall.entity.dto.CreateCouponDTO;
import com.fcl.ccmall.entity.dto.GetCouponListDTO;
import com.fcl.ccmall.entity.dto.UpdateCouponDTO;
import com.fcl.ccmall.model.CcCoupon;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fcl
 * @since 2023-03-28
 */
public interface CcCouponService extends IService<CcCoupon> {

    CommonResult createCoupon(CreateCouponDTO createCouponDTO);

    CommonResult updateCoupon(UpdateCouponDTO updateCouponDTO);

    CommonResult deleteCoupon(BatchRemoveDTO batchRemoveDTO);

    CommonResult getCouponList(GetCouponListDTO getCouponListDTO);

    CommonResult getCouponInfo(Integer id);

    CommonResult getCouponDetail(Integer id);
}
